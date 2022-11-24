package game.cards;

import fileio.CardInput;
import java.util.ArrayList;
import java.util.List;

public final class CardFactory {

    private static final int HEROHEALTH = 30;

    private CardFactory() {
    }

    /**
     * @param cardInput input data
     * @return Card created from input data
     */
    public static Card getCard(final CardInput cardInput) {
        List<CardProperties> cardPropertiesList = new ArrayList<>();

        switch (cardInput.getName()) {
            case "Sentinel":
            case "Berserker":
                cardPropertiesList.add(CardProperties.MINION);
                return new MinionCard(cardInput.getMana(), cardInput.getDescription(),
                    cardInput.getColors(),
                    cardInput.getName(), cardInput.getHealth(), cardInput.getAttackDamage(),
                    cardPropertiesList, null);
            case "Goliath":
            case "Warden":
                cardPropertiesList.add(CardProperties.MINION);
                cardPropertiesList.add(CardProperties.FRONTLINE);
                cardPropertiesList.add(CardProperties.TANK);
                return new MinionCard(cardInput.getMana(), cardInput.getDescription(),
                    cardInput.getColors(),
                    cardInput.getName(), cardInput.getHealth(), cardInput.getAttackDamage(),
                    cardPropertiesList, null);
            case "Miraj":
                cardPropertiesList.add(CardProperties.MINION);
                cardPropertiesList.add(CardProperties.FRONTLINE);
                return new MinionCard(cardInput.getMana(), cardInput.getDescription(),
                    cardInput.getColors(),
                    cardInput.getName(), cardInput.getHealth(), cardInput.getAttackDamage(),
                    cardPropertiesList, new Skyjack());
            case "The Ripper":
                cardPropertiesList.add(CardProperties.MINION);
                cardPropertiesList.add(CardProperties.FRONTLINE);
                return new MinionCard(cardInput.getMana(), cardInput.getDescription(),
                    cardInput.getColors(),
                    cardInput.getName(), cardInput.getHealth(), cardInput.getAttackDamage(),
                    cardPropertiesList, new WeakKnees());
            case "Disciple":
                cardPropertiesList.add(CardProperties.MINION);
                return new MinionCard(cardInput.getMana(), cardInput.getDescription(),
                    cardInput.getColors(),
                    cardInput.getName(), cardInput.getHealth(), cardInput.getAttackDamage(),
                    cardPropertiesList, new GodsPlan());
            case "The Cursed One":
                cardPropertiesList.add(CardProperties.MINION);
                return new MinionCard(cardInput.getMana(), cardInput.getDescription(),
                    cardInput.getColors(),
                    cardInput.getName(), cardInput.getHealth(), cardInput.getAttackDamage(),
                    cardPropertiesList, new Shapeshift());
            case "Firestorm":
                cardPropertiesList.add(CardProperties.ENVIRONMENT);
                return new EnvironmentCard(cardInput.getMana(), cardInput.getDescription(),
                    cardInput.getColors(),
                    cardInput.getName(), cardPropertiesList, new Firestorm());
            case "Winterfell":
                cardPropertiesList.add(CardProperties.ENVIRONMENT);
                return new EnvironmentCard(cardInput.getMana(), cardInput.getDescription(),
                    cardInput.getColors(),
                    cardInput.getName(), cardPropertiesList, new Winterfell());
            case "Heart Hound":
                cardPropertiesList.add(CardProperties.ENVIRONMENT);
                return new EnvironmentCard(cardInput.getMana(), cardInput.getDescription(),
                    cardInput.getColors(),
                    cardInput.getName(), cardPropertiesList, new HeartHound());
            case "Lord Royce":
                cardPropertiesList.add(CardProperties.HERO);
                return new HeroCard(cardInput.getMana(), cardInput.getDescription(),
                    cardInput.getColors(),
                    cardInput.getName(), HEROHEALTH, cardPropertiesList, new SubZero());
            case "Empress Thorina":
                cardPropertiesList.add(CardProperties.HERO);
                return new HeroCard(cardInput.getMana(), cardInput.getDescription(),
                    cardInput.getColors(),
                    cardInput.getName(), HEROHEALTH, cardPropertiesList, new LowBlow());
            case "King Mudface":
                cardPropertiesList.add(CardProperties.HERO);
                return new HeroCard(cardInput.getMana(), cardInput.getDescription(),
                    cardInput.getColors(),
                    cardInput.getName(), HEROHEALTH, cardPropertiesList, new EarthBorn());
            case "General Kocioraw":
                cardPropertiesList.add(CardProperties.HERO);
                return new HeroCard(cardInput.getMana(), cardInput.getDescription(),
                    cardInput.getColors(),
                    cardInput.getName(), HEROHEALTH, cardPropertiesList, new BloodThirst());
            default:
                return null;
        }
    }
}
