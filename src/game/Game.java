package game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.DecksInput;
import fileio.StartGameInput;
import game.cards.Card;
import game.cards.CardFactory;
import game.cards.CardProperties;
import game.cards.EnvironmentCard;
import game.cards.HeroCard;
import game.cards.MinionCard;
import game.cards.StatusEffects;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Game implements StateTransition {

    private State currentState;
    private List<Player> playerArr;
    private Board board;
    private int currentPlayerIdx;
    private CommandImpl commandImpl;
    private int manaPerRound = 1;
    private static final int MAXMANAPERROUND = 10;

    public Game(final DecksInput playerOneDecks, final DecksInput playerTwoDecks) {
        initGame(playerOneDecks, playerTwoDecks);
    }

    @Override
    public void initGame(final DecksInput playerOneDecks, final DecksInput playerTwoDecks) {
        playerArr = new ArrayList<>(2);

        playerArr.add(new Player(playerOneDecks));
        playerArr.add(new Player(playerTwoDecks));

        board = new Board();
        commandImpl = new CommandImpl();

        StatTracker.getInstance().resetStats();

        currentState = State.INITIALIZED;
    }

    @Override
    public void newGame() {
        if (currentState != State.INITIALIZED && currentState != State.GAMEOVER) {
            throw new IllegalStateException(
                "Cannot run newGame() in current state:" + currentState);
        }

        for (Player player : playerArr) {
            player.getHand().clear();
            player.resetMana();
            player.setEndedTurn(false);
        }

        StatTracker.getInstance().incrementTotalGamesPlayed();

        board.clear();
        manaPerRound = 1;

        currentState = State.READY;
    }

    @Override
    public void startGame(final StartGameInput startGameInput) {
        if (currentState != State.READY) {
            throw new IllegalStateException(
                "Cannot run startGame() in current state:" + currentState);
        }

        playerArr.get(0)
            .chooseDeck(startGameInput.getPlayerOneDeckIdx(), startGameInput.getShuffleSeed());
        playerArr.get(1)
            .chooseDeck(startGameInput.getPlayerTwoDeckIdx(), startGameInput.getShuffleSeed());

        playerArr.get(0)
            .setHeroCard((HeroCard) CardFactory.getCard(startGameInput.getPlayerOneHero()));
        playerArr.get(1)
            .setHeroCard((HeroCard) CardFactory.getCard(startGameInput.getPlayerTwoHero()));

        for (Player player : playerArr) {
            player.getHeroCard().setAvailable(true);
        }

        currentPlayerIdx = startGameInput.getStartingPlayer();

        currentState = State.PLAYING;

        newRound();
    }

    /**
     * starts a new round
     */
    public void newRound() {
        for (Player player : playerArr) {
            player.getDeck().dealCard(player.getHand());
            player.gainMana(manaPerRound);
            player.setEndedTurn(false);
        }

        if (manaPerRound < MAXMANAPERROUND) {
            manaPerRound++;
        }
    }


    /**
     * @param actionsInput for input
     * @param output for output
     */
    public void executeAction(final ActionsInput actionsInput, final ArrayNode output) {
        if (currentState != State.PLAYING) {
            throw new IllegalStateException(
                "Cannot run executeAction() in current state:" + currentState);
        }

        ObjectNode node = commandImpl.executeCommand(actionsInput);

        if (node != null) {
            output.add(node);
        }
    }

    @Override
    public void stopGame() {
        if (currentState != State.PLAYING) {
            throw new IllegalStateException(
                "Cannot run stopGame() in current state:" + currentState);
        }

        currentState = State.GAMEOVER;
    }

    class CommandImpl {

        private final Map<String, Command> commandsMap;
        private final ObjectMapper mapper;

        CommandImpl() {
            mapper = new ObjectMapper();
            commandsMap = new HashMap<>();

            commandsMap.put("getPlayerHero", actionsInput -> {
                ObjectNode node = mapper.createObjectNode();

                node.put("command", actionsInput.getCommand());
                node.put("playerIdx", actionsInput.getPlayerIdx());
                node.set("output", mapper.valueToTree(
                    playerArr.get(actionsInput.getPlayerIdx() - 1).getHeroCard()));

                return node;
            });

            commandsMap.put("getPlayerTurn", actionsInput -> {
                ObjectNode node = mapper.createObjectNode();

                node.put("command", actionsInput.getCommand());
                node.put("output", currentPlayerIdx);

                return node;
            });

            commandsMap.put("getPlayerDeck", actionsInput -> {
                ObjectNode node = mapper.createObjectNode();

                node.put("command", actionsInput.getCommand());
                node.put("playerIdx", actionsInput.getPlayerIdx());
                node.set("output", mapper.valueToTree(
                    playerArr.get(actionsInput.getPlayerIdx() - 1).getDeck().getCardList()));

                return node;
            });

            commandsMap.put("getCardsInHand", actionsInput -> {
                ObjectNode node = mapper.createObjectNode();

                node.put("command", actionsInput.getCommand());
                node.put("playerIdx", actionsInput.getPlayerIdx());
                node.set("output", mapper.valueToTree(
                    playerArr.get(actionsInput.getPlayerIdx() - 1).getHand().getCardList()));

                return node;
            });

            commandsMap.put("endPlayerTurn", actionsInput -> {
                for (MinionCard minionCard : board.getFrontRow(currentPlayerIdx)) {
                    minionCard.setAvailable(true);
                    minionCard.removeStatusEffect(StatusEffects.FROZEN);
                }

                for (MinionCard minionCard : board.getBackRow(currentPlayerIdx)) {
                    minionCard.setAvailable(true);
                    minionCard.removeStatusEffect(StatusEffects.FROZEN);
                }

                playerArr.get(currentPlayerIdx - 1).getHeroCard().setAvailable(true);
                playerArr.get(currentPlayerIdx - 1).setEndedTurn(true);
                currentPlayerIdx = currentPlayerIdx % 2 + 1;

                for (Player player : playerArr) {
                    if (!player.isEndedTurn()) {
                        return null;
                    }
                }

                newRound();

                return null;
            });

            commandsMap.put("placeCard", actionsInput -> {
                ObjectNode node = mapper.createObjectNode();

                node.put("command", actionsInput.getCommand());
                node.put("handIdx", actionsInput.getHandIdx());

                Card card = playerArr.get(currentPlayerIdx - 1).getHand()
                    .getCard(actionsInput.getHandIdx());

                if (card.getCardPropertiesList().contains(CardProperties.ENVIRONMENT)) {
                    node.put("error", ErrorMessages.PLACEENVCARD.toString());
                    return node;
                }

                if (card.getMana() > playerArr.get(currentPlayerIdx - 1).getMana()) {
                    node.put("error", ErrorMessages.NOTENOUGHMANAPLACE.toString());
                    return node;
                }

                List<MinionCard> row;

                if (card.getCardPropertiesList().contains(CardProperties.FRONTLINE)) {
                    row = board.getFrontRow(currentPlayerIdx);
                } else {
                    row = board.getBackRow(currentPlayerIdx);
                }

                if (board.checkRowIsFull(row)) {
                    node.put("error", ErrorMessages.FULLROW.toString());
                    return node;
                }

                playerArr.get(currentPlayerIdx - 1).spendMana(card.getMana());
                playerArr.get(currentPlayerIdx - 1).getHand().getCardList().remove(card);

                ((MinionCard) card).setAvailable(true);
                row.add((MinionCard) card);

                return null;
            });

            commandsMap.put("cardUsesAttack", actionsInput -> {
                ObjectNode node = mapper.createObjectNode();

                node.put("command", actionsInput.getCommand());
                node.set("cardAttacker", mapper.valueToTree(actionsInput.getCardAttacker()));
                node.set("cardAttacked", mapper.valueToTree(actionsInput.getCardAttacked()));

                MinionCard attackerCard = board.getRow(actionsInput.getCardAttacker().getX())
                    .get(actionsInput.getCardAttacker().getY());
                MinionCard attackedCard = board.getRow(actionsInput.getCardAttacked().getX())
                    .get(actionsInput.getCardAttacked().getY());

                if (board.getFrontRow(currentPlayerIdx).contains(attackedCard) || board.getBackRow(
                    currentPlayerIdx).contains(attackedCard)) {
                    node.put("error", ErrorMessages.ATTACKEDFRIENDMINION.toString());
                    return node;
                }

                if (!attackerCard.isAvailable()) {
                    node.put("error", ErrorMessages.MINIONNOTAVAILABLE.toString());
                    return node;
                }

                if (attackerCard.getStatusEffectsList().contains(StatusEffects.FROZEN)) {
                    node.put("error", ErrorMessages.FROZENMINION.toString());
                    return node;
                }

                if (!attackedCard.getCardPropertiesList().contains(CardProperties.TANK)) {
                    for (MinionCard minionCard : board.getFrontRow(currentPlayerIdx % 2 + 1)) {
                        if (minionCard.getCardPropertiesList().contains(CardProperties.TANK)) {
                            node.put("error", ErrorMessages.IGNOREDTANK.toString());
                            return node;
                        }
                    }
                }

                attackerCard.attackMinion(board, actionsInput.getCardAttacked());

                return null;
            });

            commandsMap.put("cardUsesAbility", actionsInput -> {
                ObjectNode node = mapper.createObjectNode();

                node.put("command", actionsInput.getCommand());
                node.set("cardAttacker", mapper.valueToTree(actionsInput.getCardAttacker()));
                node.set("cardAttacked", mapper.valueToTree(actionsInput.getCardAttacked()));

                MinionCard attackerMinion = board.getRow(actionsInput.getCardAttacker().getX())
                    .get(actionsInput.getCardAttacker().getY());
                MinionCard attackedMinion = board.getRow(actionsInput.getCardAttacked().getX())
                    .get(actionsInput.getCardAttacked().getY());

                if (attackerMinion.getStatusEffectsList().contains(StatusEffects.FROZEN)) {
                    node.put("error", ErrorMessages.FROZENMINION.toString());
                    return node;
                }

                if (!attackerMinion.isAvailable()) {
                    node.put("error", ErrorMessages.MINIONNOTAVAILABLE.toString());
                    return node;
                }

                if (attackerMinion.getName().equals("Disciple")) {
                    if (!board.getFrontRow(currentPlayerIdx).contains(attackedMinion)
                        && !board.getBackRow(currentPlayerIdx).contains(attackedMinion)) {
                        node.put("error", ErrorMessages.ATTACKEDENEMYMINION.toString());
                        return node;
                    }

                    attackerMinion.executeAbility(board, actionsInput.getCardAttacker(),
                        actionsInput.getCardAttacked());
                    return null;
                }

                if (board.getFrontRow(currentPlayerIdx).contains(attackedMinion)
                    || board.getBackRow(currentPlayerIdx).contains(attackedMinion)) {
                    node.put("error", ErrorMessages.ATTACKEDFRIENDMINION.toString());
                    return node;
                }

                if (!attackedMinion.getCardPropertiesList().contains(CardProperties.TANK)) {
                    for (MinionCard minionCard : board.getFrontRow(currentPlayerIdx % 2 + 1)) {
                        if (minionCard.getCardPropertiesList().contains(CardProperties.TANK)) {
                            node.put("error", ErrorMessages.IGNOREDTANK.toString());
                            return node;
                        }
                    }
                }

                attackerMinion.executeAbility(board, actionsInput.getCardAttacker(),
                    actionsInput.getCardAttacked());

                return null;
            });

            commandsMap.put("useAttackHero", actionsInput -> {
                ObjectNode node = mapper.createObjectNode();

                node.put("command", actionsInput.getCommand());
                node.set("cardAttacker", mapper.valueToTree(actionsInput.getCardAttacker()));

                MinionCard attackerMinion = board.getRow(actionsInput.getCardAttacker().getX())
                    .get(actionsInput.getCardAttacker().getY());

                if (attackerMinion.getStatusEffectsList().contains(StatusEffects.FROZEN)) {
                    node.put("error", ErrorMessages.FROZENMINION.toString());
                    return node;
                }

                if (!attackerMinion.isAvailable()) {
                    node.put("error", ErrorMessages.MINIONNOTAVAILABLE.toString());
                    return node;
                }

                for (MinionCard minionCard : board.getFrontRow(currentPlayerIdx % 2 + 1)) {
                    if (minionCard.getCardPropertiesList().contains(CardProperties.TANK)) {
                        node.put("error", ErrorMessages.IGNOREDTANK.toString());
                        return node;
                    }
                }

                HeroCard enemyHero = playerArr.get(currentPlayerIdx % 2).getHeroCard();

                attackerMinion.attackHero(enemyHero);

                if (enemyHero.getHealth() > 0) {
                    return null;
                }

                node.removeAll();

                if (currentPlayerIdx == 1) {
                    node.put("gameEnded", "Player one killed the enemy hero.");
                    StatTracker.getInstance().incrementPlayerOneWins();
                } else {
                    node.put("gameEnded", "Player two killed the enemy hero.");
                    StatTracker.getInstance().incrementPlayerTwoWins();
                }

                return node;
            });

            commandsMap.put("useHeroAbility", actionsInput -> {
                ObjectNode node = mapper.createObjectNode();

                node.put("command", actionsInput.getCommand());
                node.put("affectedRow", actionsInput.getAffectedRow());

                HeroCard hero = playerArr.get(currentPlayerIdx - 1).getHeroCard();

                if (hero.getMana() > playerArr.get(currentPlayerIdx - 1).getMana()) {
                    node.put("error", ErrorMessages.NOTENOUGHMANAHERO.toString());
                    return node;
                }

                if (!hero.isAvailable()) {
                    node.put("error", ErrorMessages.HERONOTAVAILABLE.toString());
                    return node;
                }

                if (hero.getName().equals("Lord Royce") || hero.getName()
                    .equals("Empress Thorina")) {
                    if (board.getRow(actionsInput.getAffectedRow())
                        .equals(board.getFrontRow(currentPlayerIdx))
                        || board.getRow(actionsInput.getAffectedRow())
                            .equals(board.getBackRow(currentPlayerIdx))) {
                        node.put("error", ErrorMessages.SELROWNOTBELONGTOENEMY.toString());
                        return node;
                    }

                    playerArr.get(currentPlayerIdx - 1).spendMana(hero.getMana());
                    hero.executeAbility(board, actionsInput.getAffectedRow());

                    return null;
                }

                if (board.getRow(actionsInput.getAffectedRow())
                    .equals(board.getFrontRow(currentPlayerIdx % 2 + 1))
                    || board.getRow(actionsInput.getAffectedRow())
                        .equals(board.getBackRow(currentPlayerIdx % 2 + 1))) {
                    node.put("error", ErrorMessages.SELROWNOTBELONGTOPLAYER.toString());
                    return node;
                }

                playerArr.get(currentPlayerIdx - 1).spendMana(hero.getMana());
                hero.executeAbility(board, actionsInput.getAffectedRow());

                return null;
            });

            commandsMap.put("useEnvironmentCard", actionsInput -> {
                ObjectNode node = mapper.createObjectNode();

                node.put("command", actionsInput.getCommand());
                node.put("handIdx", actionsInput.getHandIdx());
                node.put("affectedRow", actionsInput.getAffectedRow());

                Card card = playerArr.get(currentPlayerIdx - 1).getHand()
                    .getCard(actionsInput.getHandIdx());

                if (!card.getCardPropertiesList().contains(CardProperties.ENVIRONMENT)) {
                    node.put("error", ErrorMessages.NOTENVCARD.toString());
                    return node;
                }

                if (card.getMana() > playerArr.get(currentPlayerIdx - 1).getMana()) {
                    node.put("error", ErrorMessages.NOTENOUGHMANAENV.toString());
                    return node;
                }

                List<MinionCard> targetedRow = board.getRow(actionsInput.getAffectedRow());

                if (targetedRow.equals(board.getFrontRow(currentPlayerIdx)) || targetedRow.equals(
                    board.getBackRow(currentPlayerIdx))) {
                    node.put("error", ErrorMessages.ROWDOESNOTBELONGTOENEMY.toString());
                    return node;
                }

                if (card.getName().equals("Heart Hound")) {
                    if (board.checkRowIsFull(board.getMirroredRow(actionsInput.getAffectedRow()))) {
                        node.put("error", ErrorMessages.CANNOTSTEALCARDFULLROW.toString());
                        return node;
                    }
                }

                ((EnvironmentCard) card).executeAbility(board, actionsInput.getAffectedRow());
                playerArr.get(currentPlayerIdx - 1).spendMana(card.getMana());
                playerArr.get(currentPlayerIdx - 1).getHand().removeCard(actionsInput.getHandIdx());

                return null;
            });

            commandsMap.put("getCardsOnTable", actionsInput -> {
                ObjectNode node = mapper.createObjectNode();

                node.put("command", actionsInput.getCommand());
                node.set("output", mapper.valueToTree(board.getMinionCardMatrix()));

                return node;
            });

            commandsMap.put("getCardAtPosition", actionsInput -> {
                ObjectNode node = mapper.createObjectNode();

                node.put("command", actionsInput.getCommand());
                node.put("x", actionsInput.getX());
                node.put("y", actionsInput.getY());

                if (actionsInput.getY() >= board.getMinionCardMatrix().get(actionsInput.getX())
                    .size()) {
                    node.put("output", ErrorMessages.NOCARDATPOSITION.toString());
                } else {
                    node.set("output", mapper.valueToTree(
                        board.getMinionCardMatrix().get(actionsInput.getX())
                            .get(actionsInput.getY())));
                }

                return node;
            });

            commandsMap.put("getPlayerMana", actionsInput -> {
                ObjectNode node = mapper.createObjectNode();

                node.put("command", actionsInput.getCommand());
                node.put("playerIdx", actionsInput.getPlayerIdx());
                node.put("output", playerArr.get(actionsInput.getPlayerIdx() - 1).getMana());

                return node;
            });

            commandsMap.put("getEnvironmentCardsInHand", actionsInput -> {
                ObjectNode node = mapper.createObjectNode();

                node.put("command", actionsInput.getCommand());
                node.put("playerIdx", actionsInput.getPlayerIdx());

                List<Card> envCards = new ArrayList<>();

                for (Card card : playerArr.get(actionsInput.getPlayerIdx() - 1).getHand()
                    .getCardList()) {
                    if (card.getCardPropertiesList().contains(CardProperties.ENVIRONMENT)) {
                        envCards.add(card);
                    }
                }

                node.set("output", mapper.valueToTree(envCards));

                return node;
            });

            commandsMap.put("getFrozenCardsOnTable", actionsInput -> {
                ObjectNode node = mapper.createObjectNode();

                node.put("command", actionsInput.getCommand());

                List<MinionCard> frozenCards = new ArrayList<>();

                for (List<MinionCard> minionCardList : board.getMinionCardMatrix()) {
                    for (MinionCard minionCard : minionCardList) {
                        if (minionCard.getStatusEffectsList().contains(StatusEffects.FROZEN)) {
                            frozenCards.add(minionCard);
                        }
                    }
                }

                node.set("output", mapper.valueToTree(frozenCards));

                return node;
            });

            commandsMap.put("getTotalGamesPlayed", actionsInput -> {
                ObjectNode node = mapper.createObjectNode();

                node.put("command", actionsInput.getCommand());
                node.put("output", StatTracker.getInstance().getTotalGamesPlayed());

                return node;
            });

            commandsMap.put("getPlayerOneWins", actionsInput -> {
                ObjectNode node = mapper.createObjectNode();

                node.put("command", actionsInput.getCommand());
                node.put("output", StatTracker.getInstance().getPlayerOneWins());

                return node;
            });

            commandsMap.put("getPlayerTwoWins", actionsInput -> {
                ObjectNode node = mapper.createObjectNode();

                node.put("command", actionsInput.getCommand());
                node.put("output", StatTracker.getInstance().getPlayerTwoWins());

                return node;
            });
        }

        public ObjectNode executeCommand(final ActionsInput actionsInput) {
            Command command = commandsMap.get(actionsInput.getCommand());

            if (command == null) {
                throw new IllegalArgumentException("Invalid command: " + actionsInput.getCommand());
            }

            return command.execute(actionsInput);
        }
    }
}
