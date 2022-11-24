package game.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.Coordinates;
import game.Board;
import java.util.ArrayList;
import java.util.List;

public final class MinionCard extends Card {

    @JsonIgnore
    @lombok.Getter
    private final List<StatusEffects> statusEffectsList;
    private final MinionTargetedAbility ability;
    @lombok.Getter
    @lombok.Setter
    private int health;
    @lombok.Getter
    @lombok.Setter
    private int attackDamage;
    @JsonIgnore
    @lombok.Getter
    @lombok.Setter
    private boolean available;

    public MinionCard(final int manaCost, final String description, final List<String> colors,
        final String name,
        final int health, final int attackDamage, final List<CardProperties> cardPropertiesList,
        final MinionTargetedAbility ability) {
        super(manaCost, description, colors, name, cardPropertiesList);
        this.health = health;
        this.attackDamage = attackDamage;
        this.ability = ability;
        statusEffectsList = new ArrayList<>();
    }

    public MinionCard(final MinionCard minionCard) {
        super(minionCard);
        this.health = minionCard.health;
        this.attackDamage = minionCard.attackDamage;
        this.ability = minionCard.ability;
        statusEffectsList = new ArrayList<>();
        this.available = minionCard.available;
    }

    @Override
    public Card copy() {
        return new MinionCard(this);
    }

    /**
     * @param statusEffect status effect
     *
     * adds stsatus effect to status effect list
     */
    public void addStatusEffect(final StatusEffects statusEffect) {
        if (!statusEffectsList.contains(statusEffect)) {
            statusEffectsList.add(statusEffect);
        }
    }

    /**
     * @param statusEffect status effect
     *
     * removes status effect from status effect list
     */
    public void removeStatusEffect(final StatusEffects statusEffect) {
        statusEffectsList.remove(statusEffect);
    }

    /**
     * @param damage damage amount
     *
     * subtracts damage from health
     */
    public void takeDamage(final int damage) {
        health -= damage;
    }

    /**
     * @param board game board
     * @param attackedMinionCoordinates board coordinates of attacked minion
     *
     * attacks minion at given coordinates
     */
    public void attackMinion(final Board board, final Coordinates attackedMinionCoordinates) {
        MinionCard attackedMinion = board.getRow(attackedMinionCoordinates.getX())
            .get(attackedMinionCoordinates.getY());

        attackedMinion.takeDamage(attackDamage);

        if (attackedMinion.getHealth() <= 0) {
            board.getRow(attackedMinionCoordinates.getX()).remove(attackedMinion);
        }

        available = false;
    }

    /**
     * @param board game board
     * @param attackerMinionCoord board coordinates of attacker minion
     * @param targetedMinionCoord board coordinates of attacked minion
     *
     * executes ability on attacked minion
     */
    public void executeAbility(final Board board, final Coordinates attackerMinionCoord,
        final Coordinates targetedMinionCoord) {
        if (ability == null) {
            return;
        }

        ability.execute(board, attackerMinionCoord, targetedMinionCoord);
        available = false;
    }

    /**
     * @param heroCard hero
     *
     * attacks hero
     */
    public void attackHero(final HeroCard heroCard) {
        heroCard.takeDamage(attackDamage);
        available = false;
    }
}
