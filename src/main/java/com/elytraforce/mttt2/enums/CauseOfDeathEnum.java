package com.elytraforce.mttt2.enums;

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
	
	 
	
	
	
}
