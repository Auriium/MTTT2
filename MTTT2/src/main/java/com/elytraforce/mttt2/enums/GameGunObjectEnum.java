package main.java.com.elytraforce.mttt2.enums;

public enum GameGunObjectEnum
{
	PRIMARY, SECONDARY, ABILITY;

	private static GameGunObjectEnum state;

	public static void setState(GameGunObjectEnum state)
		{
			GameGunObjectEnum.state = state;
		}

	public static boolean isState(GameGunObjectEnum state)
		{
			return GameGunObjectEnum.state == state;
		}

	public static GameGunObjectEnum getState()
		{ 
			return state;
		}
}
