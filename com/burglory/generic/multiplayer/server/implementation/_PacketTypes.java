package com.burglory.generic.multiplayer.server.implementation;

public class _PacketTypes {

	private static final int StartingRange = 0;
	private static final int RangeDiff = 100;

	public static boolean isServerPacket(int value) {
		return value > 0;
	}

	public static boolean isClientPacket(int value) {
		return value < 0;
	}

	public static boolean isConnType(int value) {
		if (value < 0)
			value = value * -1;
		return (value > ConnServer.ConnTypes.range && value < ConnServer.ConnTypes.range
				+ RangeDiff);
	}

	public static boolean isAuthType(int value) {
		if (value < 0)
			value = value * -1;
		return (value > ConnServer.AuthTypes.range && value < ConnServer.AuthTypes.range
				+ RangeDiff);
	}

	public static boolean isLobbyType(int value) {
		if (value < 0)
			value = value * -1;
		return (value > ConnServer.LobbyTypes.range && value < ConnServer.LobbyTypes.range
				+ RangeDiff);
	}

	public static boolean isChatType(int value) {
		if (value < 0)
			value = value * -1;
		return (value > ConnServer.ChatTypes.range && value < ConnServer.ChatTypes.range
				+ RangeDiff);
	}

	public static class ConnServer {
		public static class ConnTypes {
			private static final int range = StartingRange;

			/** Tell a {@link User} to disconnect. */
			public static final int Disconnect = range + 1;

			/** Tell a {@link User} another {@link User} has changed its state. */
			public static final int UserStateChange = range + 3;

		}

		public static class AuthTypes {
			private static final int range = ConnTypes.range + RangeDiff;

			/** Respond to an authentication request. */
			public static final int AuthReponse = range + 1;

			/** Respond to a registration request. */
			public static final int RegResponse = range + 2;
		}

		public static class LobbyTypes {
			private static final int range = AuthTypes.range + RangeDiff;
			public static final int JoinLobbyResponse = range + 1;
			public static final int CreateLobbyResponse = range + 2;
			public static final int LobbyStateChange = range + 3;
			public static final int AdminStateChange = range + 4;
			public static final int UserStateChange = range + 5;
		}

		public static class ChatTypes {
			private static final int range = LobbyTypes.range + RangeDiff;
			public static final int ChatMessage = range + 1;
		}
	}

	public static class ConnClient {
		public static class ConnTypes {
			private static final int range = -1 * ConnServer.ConnTypes.range;
			public static final int Disconnect = range - 1;

		}

		public static class AuthTypes {
			private static final int range = -1 * ConnServer.AuthTypes.range;
			public static final int AuthRequest = range - 1;
			public static final int RegRequest = range - 2;
		}

		public static class LobbyTypes {
			private static final int range = -1 * ConnServer.LobbyTypes.range;
			public static final int JoinLobbyRequest = range - 1;
			public static final int CreateLobbyRequest = range - 2;
			public static final int DisbandLobby = range - 3;
			public static final int KickLobby = range - 4;
			public static final int LeaveLobby = range - 5;
		}

		public static class ChatTypes {
			private static final int range = -1 * ConnServer.ChatTypes.range;
			public static final int ChatMessage = range - 1;
		}
	}

}
