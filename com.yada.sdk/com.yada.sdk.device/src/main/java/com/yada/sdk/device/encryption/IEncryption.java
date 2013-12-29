package com.yada.sdk.device.encryption;

public interface IEncryption {
	/***
	 * 获取受本地主密钥（LMK）保护的终端主密钥（TMK）
	 * @param zmkTmk 受区域主密钥（LMK）保护的终端主密钥（TMK）
	 * @return 受本地主密钥（LMK）保护的终端主密钥（TMK）
	 */
	public String getLmkTmk(String zmkTmk);
	/***
	 * 获取受本地主密钥（LMK）保护的终端认证密钥（TMK）
	 * @param lmkTmk 受本地主密钥（LMK）保护的终端主密钥（TMK）
	 * @param tmkTak 受终端主密钥（TMK）保护的终端认证密钥（TAK）
	 * @return 受本地主密钥（LMK）保护的终端认证密钥（TAK）
	 */
	public String getLmkTak(String lmkTmk, String tmkTak);
	/***
	 * 获取受本地主密钥（LMK）保护的终端PIN密钥（TPK）
	 * @param lmkTmk 受本地主密钥（LMK）保护的终端主密钥（TMK）
	 * @param tmkTpk 受终端主密钥（TMK）保护的终端PIN密钥（TPK）
	 * @return 受本地主密钥（LMK）保护的终端PIN密钥（TPK）
	 */
	public String getLmkTpk(String lmkTmk, String tmkTpk);
	/***
	 * 获取受终端主密钥（TPK）保护的PIN
	 * @param accountNo 账号
	 * @param pin PIN
	 * @return 受终端主密钥（TPK）保护的PIN
	 */
	public String getTpkPin(String accountNo, String pin, String lmkTpk);
	/***
	 * 获取MAC
	 * @param macData mac验证的数据
	 * @param lmkTak 受本地主密钥（LMK）保护的终端认证密钥（TAK）
	 * @return MAC
	 */
	public String getTakMac(String macData, String lmkTak);
}
