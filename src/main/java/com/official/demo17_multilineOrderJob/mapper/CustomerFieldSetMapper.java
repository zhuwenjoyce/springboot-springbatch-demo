package com.official.demo17_multilineOrderJob.mapper;

import com.official.demo17_multilineOrderJob.domain.Customer;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class CustomerFieldSetMapper implements FieldSetMapper<Customer> {

	public static final String LINE_ID_COLUMN = "LINE_ID";
	public static final String COMPANY_NAME_COLUMN = "COMPANY_NAME";
	public static final String LAST_NAME_COLUMN = "LAST_NAME";
	public static final String FIRST_NAME_COLUMN = "FIRST_NAME";
	public static final String MIDDLE_NAME_COLUMN = "MIDDLE_NAME";
	public static final String TRUE_SYMBOL = "T";
	public static final String REGISTERED_COLUMN = "REGISTERED";
	public static final String REG_ID_COLUMN = "REG_ID";
	public static final String VIP_COLUMN = "VIP";

	@Override
	public Customer mapFieldSet(FieldSet fieldSet) {
		Customer customer = new Customer();

		if (Customer.LINE_ID_BUSINESS_CUST.equals(fieldSet.readString(LINE_ID_COLUMN))) {
			customer.setCompanyName(fieldSet.readString(COMPANY_NAME_COLUMN));
			// business customer must be always registered
			customer.setRegistered(true);
		}

		if (Customer.LINE_ID_NON_BUSINESS_CUST.equals(fieldSet.readString(LINE_ID_COLUMN))) {
			customer.setLastName(fieldSet.readString(LAST_NAME_COLUMN));
			customer.setFirstName(fieldSet.readString(FIRST_NAME_COLUMN));
			customer.setMiddleName(fieldSet.readString(MIDDLE_NAME_COLUMN));
			customer.setRegistered(TRUE_SYMBOL.equals(fieldSet.readString(REGISTERED_COLUMN)));
		}

		customer.setRegistrationId(fieldSet.readLong(REG_ID_COLUMN));
		customer.setVip(TRUE_SYMBOL.equals(fieldSet.readString(VIP_COLUMN)));

		return customer;
	}
}
