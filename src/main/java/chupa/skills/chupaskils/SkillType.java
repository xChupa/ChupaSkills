package chupa.skills.chupaskils;

import org.bukkit.Material;

enum SkillType {
HEALTH((byte)0,(byte)10,(byte)10,Material.TOTEM),DAMAGE((byte)0,(byte)10,(byte)11,Material.IRON_SWORD),SPEED((byte)0,(byte)10,(byte)12,Material.IRON_BOOTS),KRIT((byte)0,(byte)10,(byte)14,Material.REDSTONE),KRITCHANCE((byte)0,(byte)10,(byte)15,Material.BLAZE_POWDER),TAKEOVER((byte)0,(byte)10,(byte)16,Material.IRON_CHESTPLATE),MESSAGES((byte)1,(byte)1,(byte)-1,null);
	protected final byte defaultvalue,maxlevel,menuplace;
	protected final Material item;
	private SkillType(byte defaultvalue,byte maxlevel,byte menuplace,Material item) {
		this.defaultvalue=defaultvalue;
		this.maxlevel=maxlevel;
		this.menuplace=menuplace;
		this.item=item;
	}
	protected static SkillType getByMenuplace(byte menuplace) {
		for(SkillType skilltype : values()) {
			if(skilltype.menuplace==menuplace) {
				return skilltype;
			}
		}
		return null;
	}
}
