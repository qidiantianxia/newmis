package com.yada.sdk.device.encryption;

import java.nio.ByteBuffer;

public interface IEncryption {
	/***
	 * 获取受本地主密钥（LMK）保护的终端主密钥（TMK）
	 * 
	 * @param zmkTmk
	 *            受区域主密钥（LMK）保护的终端主密钥（TMK）
	 * @return 受本地主密钥（LMK）保护的终端主密钥（TMK）
	 */
	public String getLmkTmk(String zmkTmk);

	/***
	 * 获取受本地主密钥（LMK）保护的终端认证密钥（TMK）
	 * 
	 * @param lmkTmk
	 *            受本地主密钥（LMK）保护的终端主密钥（TMK）
	 * @param tmkTak
	 *            受终端主密钥（TMK）保护的终端认证密钥（TAK）
	 * @return 受本地主密钥（LMK）保护的终端认证密钥（TAK）
	 */
	public String getLmkTak(String lmkTmk, String tmkTak);

	/***
	 * 获取受本地主密钥（LMK）保护的终端PIN密钥（TPK）
	 * 
	 * @param lmkTmk
	 *            受本地主密钥（LMK）保护的终端主密钥（TMK）
	 * @param tmkTpk
	 *            受终端主密钥（TMK）保护的终端PIN密钥（TPK）
	 * @return 受本地主密钥（LMK）保护的终端PIN密钥（TPK）
	 */
	public String getLmkTpk(String lmkTmk, String tmkTpk);

	/***
	 * 获取受终端主密钥（TPK）保护的PIN
	 * 
	 * @param accountNo
	 *            账号
	 * @param pin
	 *            PIN
	 * @return 受终端主密钥（TPK）保护的PIN
	 */
	public String getTpkPin(String accountNo, String pin, String lmkTpk);

	/***
	 * 获取MAC
	 * 
	 * @param macData
	 *            mac验证的数据
	 * @param lmkTak
	 *            受本地主密钥（LMK）保护的终端认证密钥（TAK）
	 * @return MAC
	 */
	public ByteBuffer getTakMac(ByteBuffer macData, String lmkTak);
	
	/***
	 * 获取将ZPK保护的PIN密文转为受本地LMK密钥（LMK）保护的pin的密文
	 * 
	 * @param hsmZpkLmk
	 *            受区域主密钥（LMK）保护的PIN主密钥（ZpkLmk）
	 * @param classifiedPin
	 * 				受本地PIN密钥（ZpkLmk）保护的pin的密文
	 * @param cardNo
	 * 				卡号/主账号
	 * @return 受本地PIN密钥（ZpkLmk）保护的pin的明文
	 * @throws Exception 
	 */
	public String getLmkPinFromZpkPin(String hsmZpkLmk, String classifiedPin, String cardNo);
	
	/***
	 * 获取受主密钥（Lmk）保护的pin的明文
	 * 
	 * @param classifiedPin
	 * 				主密钥（Lmk）保护的pin的密文
	 * @param cardNo
	 *            卡号/主账号
	 * @return 受本地主密钥（Lmk）保护的pin的明文s
	 * 
	 * @author ZhangYaMin
	 */
	public String DecryptPin(String classifiedPin, String cardNo);
	
	/***
	 * 获取受主密钥(LMK)保护的PIN密钥对
	 * @param hsmKeyLmk
	 * 				区域主密钥（LMK）
	 * @return 返回PIN密钥对字符串
	 * @author ZhangYaMin
	 */
	public String GetLmkZpk(String hsmKeyLmk);
	
	/***
	 * 获取受主密钥(LMK)保护的MAC密钥对
	 * @param hsmKeyLmk
	 * 				区域主密钥（LMK）
	 * @return 返回MAC密钥对字符串
	 * @author ZhangYaMin
	 */
	public String GetLmkZak(String hsmKeyLmk);
	
	/***
	 * 获取受主密钥(LMK)保护的ZEK密钥对
	 * @param hsmKeyLmk
	 * 				区域主密钥（LMK）
	 * @return 返回ZEK密钥对字符串
	 * @author ZhangYaMin
	 */
	public String GetLmkZek(String hsmKeyLmk);
	
	/***
	 * 通过Zak获取MAC
	 * 
	 * @param lmkZak
	 *            受本地主密钥（LMK）保护的密钥（ZAK）
	 * @param macData
	 *            mac验证的数据
	 * @return MAC
	 * @author ZhangYaMin
	 */
	public String getZakMac(String lmkZak, String macData);
	
}
