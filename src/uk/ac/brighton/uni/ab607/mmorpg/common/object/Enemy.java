package uk.ac.brighton.uni.ab607.mmorpg.common.object;

import uk.ac.brighton.uni.ab607.mmorpg.common.AttributeInfo;
import uk.ac.brighton.uni.ab607.mmorpg.common.GameCharacter;
import uk.ac.brighton.uni.ab607.mmorpg.common.GameCharacterClass;
import uk.ac.brighton.uni.ab607.mmorpg.common.Player;
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

    public transient final EnemyType type;

    private transient Element element;

    private transient DroppableItem[] drops;

    /*package-private*/ Enemy(String id, String name, String description, EnemyType type, Element element, int level, AttributeInfo attrs, Experience xp, int spriteID, DroppableItem... drops) {
        super(name, description, GameCharacterClass.MONSTER);
        this.id = id;
        this.type = type;
        this.element = element;
        this.baseLevel = level;
        this.xp = xp;
        this.spriteID = spriteID;
        this.drops = drops;
        attributes[STR] = (byte) attrs.str;
        attributes[VIT] = (byte) attrs.vit;
        attributes[DEX] = (byte) attrs.dex;
        attributes[AGI] = (byte) attrs.agi;
        attributes[INT] = (byte) attrs.int_;
        attributes[WIS] = (byte) attrs.wis;
        attributes[WIL] = (byte) attrs.wil;
        attributes[PER] = (byte) attrs.per;
        attributes[LUC] = (byte) attrs.luc;
        calculateStats();
        setHP((int)getTotalStat(MAX_HP));   // set current hp/sp to max
        setSP((int)getTotalStat(MAX_SP));
    }

    /*package-private*/ Enemy(Enemy copy) {
        this(copy.id, copy.name, copy.description, copy.type, copy.element, copy.baseLevel,
                new AttributeInfo().str(copy.getBaseAttribute(STR))
                .vit(copy.getBaseAttribute(VIT))
                .dex(copy.getBaseAttribute(DEX))
                .agi(copy.getBaseAttribute(AGI))
                .int_(copy.getBaseAttribute(INT))
                .wis(copy.getBaseAttribute(WIS))
                .wil(copy.getBaseAttribute(WIL))
                .per(copy.getBaseAttribute(PER))
                .luc(copy.getBaseAttribute(LUC)), copy.xp, copy.spriteID, copy.drops);
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

    /**
     *
     * @param p
     *           The player who landed the killing blow
     * @return
     */
    public Chest onDeath(Player p) {
        alive = false;
        Chest drop = new Chest(x, y, GameMath.random(this.baseLevel * 100));
        for (DroppableItem item : drops) {
            if (GameMath.checkChance(item.dropChance)) {
                drop.addItem(ObjectManager.getItemByID(item.itemID));

                p.getInventory().addItem(ObjectManager.getItemByID(item.itemID));
            }
        }
        return drop;
    }

    /**
     *
     * @return
     *          Experience object containing base/stat/job xp
     *          for this enemy
     */
    public Experience getXP() {
        return xp;
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
