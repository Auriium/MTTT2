package main.java.com.elytraforce.mttt2.enums;

public enum GamePlayerRoleEnum
{
	INNOCENT, TRAITOR, DETECTIVE, NONE, SPECTATOR;

	private static GamePlayerRoleEnum state;

	public static void setState(GamePlayerRoleEnum state)
		{
			GamePlayerRoleEnum.state = state;
		}

	public static boolean isState(GamePlayerRoleEnum state)
		{
			return GamePlayerRoleEnum.state == state;
		}

	public static GamePlayerRoleEnum getState()
		{
			return state;
		}
}
