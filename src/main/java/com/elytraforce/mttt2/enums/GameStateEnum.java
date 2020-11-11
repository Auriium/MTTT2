package com.elytraforce.mttt2.enums;

public enum GameStateEnum
{
	WAITING, COUNTDOWN, PRE_MATCH, MATCH, ENDING, RESETTING;

	private static GameStateEnum state;

	public static void setState(GameStateEnum state)
		{
			GameStateEnum.state = state;
		}

	public static boolean isState(GameStateEnum state)
		{
			return GameStateEnum.state == state;
		}

	public static GameStateEnum getState()
		{
			return state;
		}
}
