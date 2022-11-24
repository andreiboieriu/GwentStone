package game;

public enum ErrorMessages {
    NOCARDATPOSITION("No card available at that position."),
    PLACEENVCARD("Cannot place environment card on table."),
    NOTENOUGHMANAPLACE("Not enough mana to place card on table."),
    FULLROW("Cannot place card on table since row is full."),
    NOTENVCARD("Chosen card is not of type environment."),
    NOTENOUGHMANAENV("Not enough mana to use environment card."),
    ROWDOESNOTBELONGTOENEMY("Chosen row does not belong to the enemy."),
    CANNOTSTEALCARDFULLROW("Cannot steal enemy card since the player's row is full."),
    ATTACKEDFRIENDMINION("Attacked card does not belong to the enemy."),
    MINIONNOTAVAILABLE("Attacker card has already attacked this turn."),
    FROZENMINION("Attacker card is frozen."),
    IGNOREDTANK("Attacked card is not of type 'Tank'."),
    ATTACKEDENEMYMINION("Attacked card does not belong to the current player."),
    NOTENOUGHMANAHERO("Not enough mana to use hero's ability."),
    HERONOTAVAILABLE("Hero has already attacked this turn."),
    SELROWNOTBELONGTOENEMY("Selected row does not belong to the enemy."),
    SELROWNOTBELONGTOPLAYER("Selected row does not belong to the current player.");

    private final String message;

    ErrorMessages(final String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
