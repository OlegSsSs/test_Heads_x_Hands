import java.util.Random;

public class BaseHero {
    private String name;
    private int attack;
    private int defence;
    private int health;
    private int minDamage;
    private int maxDamage;
    private int maxHealthAttempts;
    private double healPercent;

    public BaseHero(String name, int attack, int defence, int health, int minDamage, int maxDamage, int maxHealthAttempts, double healPercent) {
        this.name = name;
        this.attack = attack;
        this.defence = defence;
        this.health = health;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.maxHealthAttempts = maxHealthAttempts;
        this.healPercent = healPercent;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if(health < 0) {
            health = 0;
        }
    }

    @Override
    public String toString() {
        return String.format("Команда: %s Атака: %d Защита: %d Здоровье: %d",
                this.name, this.attack, this.defence, this.health);
    }
    // Рассчитываем Модификатор атаки. Он равен разности Атаки атакующего и Защиты защищающегося плюс 1
    public int calculateAttackModifier(BaseHero target) {
        int attackModifier = attack - target.defence + 1;
        return Math.max(attackModifier, 1);
    }

    // Успех определяется броском N кубиков с цифрами от 1 до 6, где N - это Модификатор атаки. Всегда бросается хотя бы один кубик.
    public static class Dice {
        private Random random = new Random();
        public int roll(int sides) {
            return random.nextInt(sides) + 1;
        }
    }
    // Удар считается успешным, если хотя бы на одном из кубиков выпадает 5 или 6
    public boolean attack(BaseHero target, Dice dice) {
        int attackModifier = calculateAttackModifier(target);
        int successfulRolls = 0;
        for (int i = 0; i < attackModifier; i++) {
            int rollResult = dice.roll(6);
            if (rollResult >= 5) {
                successfulRolls++;
            }
        }
        if (successfulRolls > 0) {
            int damage = dice.roll(maxDamage - minDamage + 1) + minDamage;
            target.takeDamage(damage);
            System.out.println(getName() + " успешно атакует " + target.getName() + " и нансоит " + damage +
                    " урона, у него осталось - " + target.getHealth() + " здоровья " );
            return true;
        } else {
            System.out.println(getName() + " не попадает по " + target.getName());
            return false;
        }
    }

    public void heal() {
        if (maxHealthAttempts > 0) {
            int maxHealAmount = (int)(health * healPercent);
            int healAmount = new Random().nextInt(maxHealAmount) + 1;
            health += healAmount;
            System.out.println(getName() + " исцелит себя на " + healAmount + " едениц здоровья " + " | Здоровье " + getHealth());
            maxHealthAttempts--;
        } else {
            System.out.println(getName() + " больше не может исцеляться");
        }
    }
}
