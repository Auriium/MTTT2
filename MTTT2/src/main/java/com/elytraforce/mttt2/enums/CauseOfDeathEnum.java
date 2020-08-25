package main.java.com.elytraforce.mttt2.enums;

import net.md_5.bungee.api.ChatColor;

public enum CauseOfDeathEnum
{
	INNOCENT, TRAITOR, DETECTIVE, FALLING, BURNING, UNKNOWN;

	private static CauseOfDeathEnum state;

	public static void setState(CauseOfDeathEnum state)
		{
		CauseOfDeathEnum.state = state;
		}

	public static boolean isState(CauseOfDeathEnum state)
		{
			return CauseOfDeathEnum.state == state;
		}

	public static CauseOfDeathEnum getState()
		{
			return state;
		}
	
	public String getFormattedString() {
		
		switch(this.state) {
		case BURNING:
			return parseColor("&c&lBURNING");
		case DETECTIVE:
			return parseColor("&9&lDETECTIVE");
		case FALLING:
			return parseColor("&f&lFALLING");
		case INNOCENT:
			return parseColor("&a&lINNOCENT");
		case TRAITOR:
			return parseColor("&4&lTRAITOR");     
		case UNKNOWN:
			return parseColor("&8&lUNKNOWN");
		default:
			break;
		}
		
		return null;
	}
	
	public String parseColor(String input) {
		return ChatColor.translateAlternateColorCodes('&', input);
	}
	
}
