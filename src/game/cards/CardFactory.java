package game.cards;

import fileio.CardInput;

public class CardFactory {
    private static int heroHealth = 30;

    public static Card getCard(CardInput cardInput) {
        switch(cardInput.getName()) {
            case "Sentinel":
            case "Berserker":
            case "Goliath":
            case "Warden":
                return new MinionCard(cardInput.getMana(), cardInput.getDescription(), cardInput.getColors(),
                                      cardInput.getName(), cardInput.getHealth(), cardInput.getAttackDamage(),
                                null);
            case "Miraj":
                return new MinionCard(cardInput.getMana(), cardInput.getDescription(), cardInput.getColors(),
                                      cardInput.getName(), cardInput.getHealth(), cardInput.getAttackDamage(),
                                      new Skyjack());
            case "The Ripper":
                return new MinionCard(cardInput.getMana(), cardInput.getDescription(), cardInput.getColors(),
                                      cardInput.getName(), cardInput.getHealth(), cardInput.getAttackDamage(),
                                      new WeakKnees());
            case "Disciple":
                return new MinionCard(cardInput.getMana(), cardInput.getDescription(), cardInput.getColors(),
                                      cardInput.getName(), cardInput.getHealth(), cardInput.getAttackDamage(),
                                      new GodsPlan());
            case "The Cursed One":
                return new MinionCard(cardInput.getMana(), cardInput.getDescription(), cardInput.getColors(),
                                      cardInput.getName(), cardInput.getHealth(), cardInput.getAttackDamage(),
                                      new Shapeshift());
            case "Firestorm":
                return new EnvironmentCard(cardInput.getMana(), cardInput.getDescription(), cardInput.getColors(),
                                           cardInput.getName(), new Firestorm());
            case "Winterfell":
                return new EnvironmentCard(cardInput.getMana(), cardInput.getDescription(), cardInput.getColors(),
                                           cardInput.getName(), new Winterfell());
            case "Heart Hound":
                return new EnvironmentCard(cardInput.getMana(), cardInput.getDescription(), cardInput.getColors(),
                                           cardInput.getName(), new HeartHound());
            case "Lord Royce":
                return new HeroCard(cardInput.getMana(), cardInput.getDescription(), cardInput.getColors(),
                                    cardInput.getName(), heroHealth, new SubZero());
            case "Empress Thorina":
                return new HeroCard(cardInput.getMana(), cardInput.getDescription(), cardInput.getColors(),
                                    cardInput.getName(), heroHealth, new LowBlow());
            case "King Mudface":
                return new HeroCard(cardInput.getMana(), cardInput.getDescription(), cardInput.getColors(),
                                    cardInput.getName(), heroHealth, new EarthBorn());
            case "General Kocioraw":
                return new HeroCard(cardInput.getMana(), cardInput.getDescription(), cardInput.getColors(),
                                    cardInput.getName(), heroHealth, new BloodThirst());
            default:
                return null;
        }
    }
}
