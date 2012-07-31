package la2.auth.net.server;

import net.io.SendablePacket;

/**
 *	TODO check on other system
 *	1 - System error
 *	2 - System error, please log in again later.
 * 	3 - Password does match this account. Confirm...
 *  4 - Password does match this account. Confirm... 
 *	5 - Account information is incorrect
 *	6 - Access failed.Please try again later...
 *	7 - this account already in use.Access denied
 *	8 - Access failed.Please try again later...
 *	9 - Access failed.Please try again later...
 *	10 - Access failed.Please try again later...
 *	11 - Access failed.Please try again later...
 *	12 - Game services may be used by individuals 15 years of age or older...
 *	13 - Password does not match this account.
 *	14 - Access failed.Please try again later...
 *  15 - Access failed.Please try again later...
 *  16 - Due to high server traffic, your login attempt has failed.
 *  17 - Server under maintenance.
 *  18 - Please login after changing your temporary password.
 *  19 - Your usage term has expired.
 *  20 - There is no time left on this account 
 *  21 - System error
 *  22 - Access failed.
 *  23 - This server is reserved for players in Korea
 *  
 *  TODO ~< 30
 */
public final class PlayFailPacket extends SendablePacket {
	
	public static enum PlayFailReason {
		REASON_SYSTEM_ERROR			(0x01),
		REASON_USER_OR_PASS_WRONG	(0x02),
		REASON3						(0x03),
		REASON4						(0x04),
		REASON_TOO_MANY_PLAYERS		(0x0f);

		public final int id;

		PlayFailReason(int id) {
			this.id = id;
		}
	}

	private final PlayFailReason reason;

	public PlayFailPacket(PlayFailReason reason) {
		this.reason = reason;
	}
	
	@Override
	public void write() {
		writeB(0x06);
		
		writeB(reason.id);
	}
}
