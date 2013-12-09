package com.yada.sdk.packages.transaction.jpos;

interface ITranIdParser {
	String getTranId(JposMessage message);
	String getOrgTranId(JposMessage message);
}
