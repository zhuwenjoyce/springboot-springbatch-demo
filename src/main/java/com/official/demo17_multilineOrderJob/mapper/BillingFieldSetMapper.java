package com.official.demo17_multilineOrderJob.mapper;

import com.official.demo17_multilineOrderJob.domain.BillingInfo;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class BillingFieldSetMapper implements FieldSetMapper<BillingInfo> {

	public static final String PAYMENT_TYPE_ID_COLUMN = "PAYMENT_TYPE_ID";
	public static final String PAYMENT_DESC_COLUMN = "PAYMENT_DESC";

	@Override
	public BillingInfo mapFieldSet(FieldSet fieldSet) {
		BillingInfo info = new BillingInfo();

		info.setPaymentId(fieldSet.readString(PAYMENT_TYPE_ID_COLUMN));
		info.setPaymentDesc(fieldSet.readString(PAYMENT_DESC_COLUMN));

		return info;
	}
}
