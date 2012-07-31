package la2.auth.net.server;

import net.io.SendablePacket;



/**
 * d: the failure reason
 * 
 * 1 - SYSTEM_FAIL
 * 2 - WRONG_PASSWORD
 * 3 - REASON_USER_OR_PASS_WRONG
 * 4 - ACCESS_FAIL
 * 5 - WRONG_INFORMATION
 * 6 - ACCESS_FAIL
 * 7 - ACCOUNT_ALREDY_IN_USE
 * 8 - ACCESS_FAIL
 * 9 - ACCESS_FAIL
 * 10 - ACCESS_FAIL
 * 11 - ACCESS_FAIL
 * 12 - NOT_18_YEAR
 * 13 - ACCESS_FAIL
 * 14 - ACCESS_FAIL
 * 15 - TO_MANY_USERS_ON_CONNECTING_TO_LOGIN_SERVER
 * 16 - SERVER_ON_DIAGNOSTIC
 * 17 - LOGIN_AFTER_CHANGE_TEMPORARY_PASSWORD
 * 18 - TIME_END_PLEASE_PAY
 * 19 - TIME_END
 * 20 - SYSTEM_ERROR
 * 21 - ACCESS_FAIL
 * 22 - THIS_SERVER_FOR_LINEAGE_2
 * 30 - TIME_END_ON_THIS_WEEK
 * 31 - WRONG_NUMBER_SECRET_CART
 * 32 - USER_WHO_DONT_PASSED_AGE_CHECK_CANT_REGISTER_IN-22:00 - 6:00
 * 33 - YOU DONT HAVE ACCESS
 * 35 - ONE_WINDOW_CAN_USE_ON_THIS_SERVER
 * 36 - ???
 * 37 - YOU_NEED_AGREE_USER_AGREEMENT_FOR_PLAY
 * 38 - NEED_AGREE_DEFENS_AGREEMET
 * 39 - YOUR_ACCOUNT_DONT_AGREEMENT
 * 40 - THIS_ACCOUNT_WAS_DENIED
 * 41 - ACCOUNT_CAN_USE_AFTER_CHANGE_PASSWORD
 * 42 - TO_MANY_WINDOW
 */
public final class LoginFailPacket extends SendablePacket {
	public static enum LoginFailReason {
		REASON_SYSTEM_ERROR			(0x01),
		REASON_PASS_WRONG			(0x02),
		REASON_USER_OR_PASS_WRONG	(0x03),
		REASON_ACCESS_FAILED		(0x04),
		REASON_ACCOUNT_IN_USE		(0x07),
		REASON_SERVER_OVERLOADED	(0x0f),
		REASON_SERVER_MAINTENANCE	(0x10),
		REASON_TEMP_PASS_EXPIRED	(0x11),
		REASON_DUAL_BOX				(0x23),
		REASON_ACCOUNT_WAS_DENIED	(0x41), 
		NULL(0);

		public final int code;

		LoginFailReason(int code) {
			this.code = code;
		}
	}

	private LoginFailReason reason;

	public LoginFailPacket(LoginFailReason reason) {
		this.reason = reason;
	}
	
	@Override
	public void write() {
		writeB(0x01);
		
		writeDW(reason.code);
	}
}
