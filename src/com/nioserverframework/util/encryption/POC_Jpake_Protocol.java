package com.nioserverframework.util.encryption;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class POC_Jpake_Protocol {

	public static void main(String[] args) {
		POC_Jpake_Protocol j = new POC_Jpake_Protocol();
		Server s = j.new Server();
		j.new Client("Frank").initiate(s);
	}

	private static final BigInteger p = new BigInteger(
			"90066455B5CFC38F9CAA4A48B4281F292C260FEEF01FD61037E56258A7795A1C7AD46076982CE6BB956936C6AB4DCFE05E6784586940CA544B9B2140E1EB523F009D20A7E7880E4E5BFA690F1B9004A27811CD9904AF70420EEFD6EA11EF7DA129F58835FF56B89FAA637BC9AC2EFAAB903402229F491D8D3485261CD068699B6BA58A1DDBBEF6DB51E8FE34E8A78E542D7BA351C21EA8D8F1D29F5D5D15939487E27F4416B0CA632C59EFD1B1EB66511A5A0FBF615B766C5862D0BD8A3FE7A0E0DA0FB2FE1FCB19E8F9996A8EA0FCCDE538175238FC8B0EE6F29AF7F642773EBE8CD5402415A01451A840476B2FCEB0E388D30D4B376C37FE401C2A2C2F941DAD179C540C1C8CE030D460C4D983BE9AB0B20F69144C1AE13F9383EA1C08504FB0BF321503EFE43488310DD8DC77EC5B8349B8BFE97C2C560EA878DE87C11E3D597F1FEA742D73EEC7F37BE43949EF1A0D15C3F3E3FC0A8335617055AC91328EC22B50FC15B941D3D1624CD88BC25F3E941FDDC6200689581BFEC416B4B2CB73",
			16);
	private static final BigInteger q = new BigInteger(
			"CFA0478A54717B08CE64805B76E5B14249A77A4838469DF7F7DC987EFCCFB11D",
			16);
	private static final BigInteger g = new BigInteger(
			"5E5CBA992E0A680D885EB903AEA78E4A45A469103D448EDE3B7ACCC54D521E37F84A4BDD5B06B0970CC2D2BBB715F7B82846F9A0C393914C792E6A923E2117AB805276A975AADB5261D91673EA9AAFFEECBFA6183DFCB5D3B7332AA19275AFA1F8EC0B60FB6F66CC23AE4870791D5982AAD1AA9485FD8F4A60126FEB2CF05DB8A7F0F09B3397F3937F2E90B9E5B9C9B6EFEF642BC48351C46FB171B9BFA9EF17A961CE96C7E7A7CC3D3D03DFAD1078BA21DA425198F07D2481622BCE45969D9C4D6063D72AB7A0F08B2F49A7CC6AF335E08C4720E31476B67299E231F8BD90B39AC3AE3BE0C6B6CACEF8289A2E2873D58E51E029CAFBD55E6841489AB66B5B4B9BA6E2F784660896AFF387D92844CCB8B69475496DE19DA2E58259B090489AC8E62363CDF82CFD8EF2A427ABCD65750B506F56DDE3B988567A88126B914D7828E2B63A6D7ED0747EC59E0E0A23CE7D8A74C1D2C2A7AFB6A29799620F00E11C33787F7DED3B30E1A22D09F1FBDA1ABBBFBF25CAE05A13F812E34563F99410E73B",
			16);

	public class Server {

		private Client client;
		private String signerID;
		private BigInteger x3;
		private BigInteger x4;
		private BigInteger gx3;
		private BigInteger gx4;
		private BigInteger[] sigX3;
		private BigInteger[] sigX4;
		private BigInteger gB;
		private BigInteger B;
		private BigInteger[] sigX4s;
		private String clientID;
		private BigInteger gx2;
		private BigInteger password;
		private byte[] h_sessionkey;
		private byte[] h_h_sessionkey;

		public Server() {
			this.signerID = "MASTERSERVER";
		}

		public void handshake(Client c) {
			this.client = c;
		}

		public void receive(Stage initiation, BigInteger gx12,
				BigInteger[] sigX12, BigInteger gx22, BigInteger[] sigX22,
				String rSA_encrypted_password, String clientID) {
			switch (initiation) {
			case INITIATION:
				this.processInitiation(gx12, sigX12, gx22, sigX22,
						rSA_encrypted_password, clientID);
			}
		}

		private void processInitiation(BigInteger gx1, BigInteger[] sigX1,
				BigInteger gx2, BigInteger[] sigX2,
				String rSA_encrypted_password, String clientID) {

			this.clientID = clientID;

			/* Server verifies Clients ZKPs */
			if (gx2.equals(BigInteger.ONE)
					|| !JPakeMethods.verifyZeroKnownProof(p, q, g, gx1, sigX1,
							clientID)
					|| !JPakeMethods.verifyZeroKnownProof(p, q, g, gx2, sigX2,
							clientID)) {
				// System.out.println("g^{x2} shoudn't be 1 or invalid KP{x1,x2}");
				System.out.println("Client failed ZKP at Stage: "
						+ Stage.INITIATION.toString());
				System.exit(0);
			} else {
				// System.out.println("Bob checks g^{x2}!=1: OK");
				// System.out.println("Bob checks KP{x1},: OK");
				// System.out.println("Bob checks KP{x2},: OK");
				// System.out.println("");
				this.gx2 = gx2;
			}

			this.password = new BigInteger(this.decrypt(rSA_encrypted_password)
					.getBytes());

			// Generate x3 in the range of [0,q-1]
			do {
				x3 = new BigInteger(160, new SecureRandom());
			} while (x3.compareTo(BigInteger.ZERO) < 0
					|| x3.compareTo(q.subtract(BigInteger.ONE)) > 0);

			// Generate x4 in the range of [1,q-1]
			do {
				x4 = new BigInteger(160, new SecureRandom());
			} while (x4.compareTo(BigInteger.ONE) < 0
					|| x4.compareTo(q.subtract(BigInteger.ONE)) > 0);

			this.gx3 = g.modPow(x3, p);
			this.gx4 = g.modPow(x4, p);

			this.sigX3 = JPakeMethods.generateZeroKnownProofs(p, q, g, gx3, x3,
					signerID);
			this.sigX4 = JPakeMethods.generateZeroKnownProofs(p, q, g, gx4, x4,
					signerID);

			this.gB = gx3.multiply(gx1).multiply(gx2).mod(p);
			this.B = gB.modPow(x4.multiply(password).mod(q), p);
			this.sigX4s = JPakeMethods.generateZeroKnownProofs(p, q, gB, B, x4
					.multiply(password).mod(q), this.signerID);

			byte[] challenge = new byte[1024];
			new SecureRandom().nextBytes(challenge);

			this.send(Stage.INITIATION_Reply, gx3, gx4, sigX3, sigX4, gB, B,
					sigX4s, challenge);
		}

		private void send(Stage initiationReply, BigInteger gx3,
				BigInteger gx4, BigInteger[] sigX3, BigInteger[] sigX4,
				BigInteger b, BigInteger gB, BigInteger[] sigX4s,
				byte[] challenge) {
			client.receive(initiationReply, gx3, gx4, sigX3, sigX4, b, gB,
					sigX4s, challenge);
		}

		private String decrypt(String rSA_encrypted_password) {
			// TODO Auto-generated method stub
			return rSA_encrypted_password;
		}

		public void receive(Stage finilization, BigInteger gA, BigInteger a,
				BigInteger[] sigX2s, byte[] h_h_sessionkey) {
			// TODO Auto-generated method stub
			switch (finilization) {
			case FINILIZATION:
				this.processFinilization(gA, a, sigX2s, h_h_sessionkey);
			}
		}

		private void processFinilization(BigInteger gA, BigInteger a,
				BigInteger[] sigX2s, byte[] h_h_sessionkey) {
			/* Server verifies Clients ZKP */
			if (!JPakeMethods.verifyZeroKnownProof(p, q, gA, a, sigX2s,
					this.clientID)) {
				// System.out.println("Invalid ZK{x2*s}");
				System.out.println("Client failed ZKP at Stage: "
						+ Stage.FINILIZATION.toString());
				System.exit(0);
			} else {
				// System.out.println("Bob checks KP{x2*s}: OK\n");
			}

			// this.sessionkey = JPakeMethods.sessionKey(gx4, x2, p, q, b,
			// password);
			String sessionkey = JPakeMethods.sessionKey(gx2, x4, p, q, a,
					password);
			MessageDigest hash = JPakeMethods.getSHA256Instance();
			this.h_sessionkey = hash.digest(sessionkey.getBytes());
			hash = JPakeMethods.getSHA256Instance();
			this.h_h_sessionkey = hash.digest(h_sessionkey);
			if (h_h_sessionkey.equals(this.h_h_sessionkey)) {
				System.out.println("JPake succesful for Client: "
						+ this.clientID);
			} else {
				System.out.println("JPake failed for Client: " + this.clientID);
			}
			this.send(Stage.FINILIZATION_Reply, this.h_sessionkey);
		}

		private void send(Stage finilizationReply, byte[] h_sessionkey) {
			client.receive(finilizationReply, h_sessionkey);
		}

	}

	public class Client {

		private Server server;
		private String signerID;
		private BigInteger gx1;
		private BigInteger gx2;
		private BigInteger[] sigX1;
		private BigInteger[] sigX2;
		private BigInteger x1;
		private BigInteger x2;
		private BigInteger gA;
		private BigInteger A;
		private BigInteger[] sigX2s;
		private String rsa_encrypted_password;
		private BigInteger password;
		private String sessionkey;
		private byte[] h_sessionkey;
		private byte[] h_h_sessionkey;

		public Client(String signerID) {
			this.signerID = signerID;
		}

		public void receive(Stage finilizationReply, byte[] h_sessionkey2) {
			switch (finilizationReply) {
			case FINILIZATION_Reply:
				this.processFinilizationReply(h_sessionkey2);
			}
		}

		private void processFinilizationReply(byte[] h_sessionkey2) {
			if (h_sessionkey2.equals(h_sessionkey)) {
				System.out.println("JPake succesful for Server: "
						+ "MASTERSERVER");
			} else {
				System.out
						.println("JPake failed for Server: " + "MASTERSERVER");
			}
		}

		public void receive(Stage initiationReply, BigInteger gx3,
				BigInteger gx4, BigInteger[] sigX3, BigInteger[] sigX4,
				BigInteger b, BigInteger gB, BigInteger[] sigX4s,
				byte[] challenge) {
			switch (initiationReply) {
			case INITIATION_Reply:
				this.processInitiationReply(gx3, gx4, sigX3, sigX4, b, gB,
						sigX4s, challenge);
			}
		}

		private void processInitiationReply(BigInteger gx3, BigInteger gx4,
				BigInteger[] sigX3, BigInteger[] sigX4, BigInteger b,
				BigInteger gB, BigInteger[] sigX4s, byte[] challenge) {

			String ServerID = "MASTERSERVER";

			/* Client verifies Servers ZKPs and also check g^{x4} != 1 */
			if (gx4.equals(BigInteger.ONE)
					|| !JPakeMethods.verifyZeroKnownProof(p, q, g, gx3, sigX3,
							"MASTERSERVER")
					|| !JPakeMethods.verifyZeroKnownProof(p, q, g, gx4, sigX4,
							"MASTERSERVER")) {
				// System.out.println("g^{x4} shouldn't be 1 or invalid KP{x3,x4}");
				System.out.println("Server failed ZKP at Stage: "
						+ Stage.INITIATION_Reply.toString());
				System.exit(0);
			} else {
				// System.out.println("Alice checks g^{x4}!=1: OK");
				// System.out.println("Alice checks KP{x3}: OK");
				// System.out.println("Alice checks KP{x4}: OK");
				// System.out.println("");
			}

			/* Client verifies Servers ZKP */
			if (!JPakeMethods.verifyZeroKnownProof(p, q, gB, b, sigX4s,
					ServerID)) {
				System.out.println("Server failed ZKP at Stage:"
						+ Stage.INITIATION_Reply.toString());
				System.exit(0);
			} else {
				// System.out.println("Alice checks KP{x4*s}: OK\n");
			}

			this.gA = gx1.multiply(gx3).multiply(gx4).mod(p);
			this.A = gA.modPow(x2.multiply(this.password).mod(q), p);
			this.sigX2s = JPakeMethods.generateZeroKnownProofs(p, q, gA, A, x2
					.multiply(this.password).mod(q), this.signerID);

			this.sessionkey = JPakeMethods.sessionKey(gx4, x2, p, q, b,
					password);

			// TODO: Vervang dit door AES.
			MessageDigest hash = JPakeMethods.getSHA256Instance();
			this.h_sessionkey = hash.digest(sessionkey.getBytes());
			hash = JPakeMethods.getSHA256Instance();
			this.h_h_sessionkey = hash.digest(h_sessionkey);

			this.send(Stage.FINILIZATION, gA, A, sigX2s, h_h_sessionkey);

		}

		private void send(Stage finilization, BigInteger gA, BigInteger a,
				BigInteger[] sigX2s, byte[] h_h_sessionkey2) {
			server.receive(finilization, gA, a, sigX2s, h_h_sessionkey);
		}

		public void initiate(Server s) {
			this.server = s;
			s.handshake(this);
			// Generate x1 in the range of [0,q-1]
			do {
				x1 = new BigInteger(160, new SecureRandom());
			} while (x1.compareTo(BigInteger.ZERO) < 0
					|| x1.compareTo(q.subtract(BigInteger.ONE)) > 0);

			// Generate x2 in the range of [1,q-1]
			do {
				x2 = new BigInteger(160, new SecureRandom());
			} while (x2.compareTo(BigInteger.ONE) < 0
					|| x2.compareTo(q.subtract(BigInteger.ONE)) > 0);

			this.gx1 = g.modPow(x1, p);
			this.gx2 = g.modPow(x2, p);

			this.sigX1 = JPakeMethods.generateZeroKnownProofs(p, q, g, gx1, x1,
					signerID);
			this.sigX2 = JPakeMethods.generateZeroKnownProofs(p, q, g, gx2, x2,
					signerID);

			this.password = new BigInteger("test".getBytes());
			this.rsa_encrypted_password = this.encrypt(password);

			this.send(Stage.INITIATION, gx1, sigX1, gx2, sigX2,
					rsa_encrypted_password, this.signerID);
		}

		private String encrypt(BigInteger password2) {
			// TODO Auto-generated method stub
			return password2.toString();
		}

		private void send(Stage initiation, BigInteger gx12,
				BigInteger[] sigX12, BigInteger gx22, BigInteger[] sigX22,
				String rSA_encrypted_password, String clientID) {
			server.receive(initiation, gx12, sigX12, gx22, sigX22,
					rSA_encrypted_password, clientID);
		}

	}

	private enum Stage {
		INITIATION, INITIATION_Reply, FINILIZATION, FINILIZATION_Reply;
	}

	private static class JPakeMethods {
		public static BigInteger[] generateZeroKnownProofs(BigInteger p,
				BigInteger q, BigInteger g, BigInteger gx, BigInteger x,
				String signerId) {
			// signerId = GetSignerId();
			BigInteger[] ZKP = new BigInteger[2];
			BigInteger v = new BigInteger(160, new SecureRandom());
			BigInteger gv = g.modPow(v, p);
			BigInteger h = getHash(g, gv, gx, signerId);

			ZKP[0] = gv;
			ZKP[1] = v.subtract(x.multiply(h)).mod(q);

			return ZKP;
		}

		public static boolean verifyZeroKnownProof(BigInteger p, BigInteger q,
				BigInteger g, BigInteger gx, BigInteger[] sig, String signerId) {
			BigInteger h = getHash(g, sig[0], gx, signerId);
			if (gx.compareTo(p.subtract(BigInteger.ZERO)) == 1
					&& gx.compareTo(p.subtract(BigInteger.ONE)) == -1
					&& gx.modPow(q, p).compareTo(BigInteger.ONE) == 0
					&& g.modPow(sig[1], p).multiply(gx.modPow(h, p)).mod(p)
							.compareTo(sig[0]) == 0)
				return true;
			else
				return false;
		}

		public static BigInteger getHash(BigInteger g, BigInteger gr,
				BigInteger gx, String signerId) {

			MessageDigest hash = getSHA256Instance();

			hash.update(g.toByteArray());
			hash.update(gr.toByteArray());
			hash.update(gx.toByteArray());
			hash.update(signerId.getBytes());
			return new BigInteger(hash.digest());

		}

		public static BigInteger getHash(BigInteger k) {
			MessageDigest hash = getSHA256Instance();
			hash.update(k.toByteArray());
			return new BigInteger(1, hash.digest());
		}

		public static MessageDigest getSHA256Instance() {
			try {
				return MessageDigest.getInstance("SHA-256");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		public static String sessionKey(BigInteger gx4, BigInteger x2,
				BigInteger p, BigInteger q, BigInteger B, BigInteger pwd) {
			BigInteger k = getHash(gx4
					.modPow(x2.multiply(pwd).negate().mod(q), p).multiply(B)
					.modPow(x2, p));
			return new String(k.toString(16));
		}
	}

}
