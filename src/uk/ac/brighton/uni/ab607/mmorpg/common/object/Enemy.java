package uk.ac.brighton.uni.ab607.mmorpg.common.object;

import uk.ac.brighton.uni.ab607.mmorpg.common.GameCharacter;
import uk.ac.brighton.uni.ab607.mmorpg.common.GameCharacterClass;
import uk.ac.brighton.uni.ab607.mmorpg.common.combat.Element;
import uk.ac.brighton.uni.ab607.mmorpg.common.item.Chest;
import uk.ac.brighton.uni.ab607.mmorpg.common.item.DroppableItem;
import uk.ac.brighton.uni.ab607.mmorpg.common.math.GameMath;

public class Enemy extends GameCharacter {

    /**
     *
     */
    private static final long serialVersionUID = -4175008430166158773L;

    public enum EnemyType {
        NORMAL, MINIBOSS, BOSS
    }

    public final EnemyType type;

    public final int experience;

    private Element element;

    private DroppableItem[] drops;

    /*package-private*/ Enemy(String id, String name, String description, EnemyType type, Element element, int level, int baseXP, String spriteName, DroppableItem... drops) {
        super(name, description, GameCharacterClass.MONSTER);
        this.id = id;
        this.type = type;
        this.element = element;
        this.baseLevel = level;
        this.experience = baseXP;
        this.spriteName = spriteName;
        this.drops = drops;
    }

    /*package-private*/ Enemy(Enemy copy) {
        this(copy.id, copy.name, copy.description, copy.type, copy.element, copy.baseLevel, copy.experience, copy.spriteName, copy.drops);
    }

    public Chest onDeath() {
        alive = false;
        Chest drop = new Chest(x, y, GameMath.random(this.baseLevel * 100));
        for (DroppableItem item : drops) {
            if (GameMath.checkChance(item.dropChance)) {
                drop.addItem(ObjectManager.getItemByID(item.itemID));
            }
        }
        return drop;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public Element getWeaponElement() {
        return element;
    }

    @Override
    public Element getArmorElement() {
        return element;
    }
}
