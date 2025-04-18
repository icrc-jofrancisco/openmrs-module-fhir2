/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.fhir2.api.dao.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.ProviderAttribute;
import org.openmrs.module.fhir2.BaseFhirContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class FhirPractitionerDaoImplTest extends BaseFhirContextSensitiveTest {
	
	private static final String PRACTITIONER_INITIAL_DATA_XML = "org/openmrs/module/fhir2/api/dao/impl/FhirPractitionerDaoImplTest_initial_data.xml";
	
	private static final String PRACTITIONER_UUID = "f9badd80-ab76-11e2-9e96-0800200c9a66";
	
	private static final String PERSON_ATTRIBUTE_TYPE_UUID = "FF89DD99-OOX78-KKG89D-XX89CC8";
	
	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;
	
	private FhirPractitionerDaoImpl dao;
	
	@Before
	public void setUp() throws Exception {
		dao = new FhirPractitionerDaoImpl();
		dao.setSessionFactory(sessionFactory);
		executeDataSet(PRACTITIONER_INITIAL_DATA_XML);
	}
	
	@Test
	public void shouldRetrievePractitionerByUuid() {
		org.openmrs.Provider provider = dao.get(PRACTITIONER_UUID);
		assertThat(provider, notNullValue());
		assertThat(provider.getUuid(), notNullValue());
		assertThat(provider.getUuid(), equalTo(PRACTITIONER_UUID));
	}
	
	@Test
	public void getActiveAttributesByPractitionerAndAttributeTypeUuid_shouldReturnPractitionerAttribute() {
		org.openmrs.Provider provider = new org.openmrs.Provider();
		provider.setUuid(PRACTITIONER_UUID);
		
		List<ProviderAttribute> attributeList = dao.getActiveAttributesByPractitionerAndAttributeTypeUuid(provider,
		    PERSON_ATTRIBUTE_TYPE_UUID);
		
		assertThat(attributeList, notNullValue());
	}
}
