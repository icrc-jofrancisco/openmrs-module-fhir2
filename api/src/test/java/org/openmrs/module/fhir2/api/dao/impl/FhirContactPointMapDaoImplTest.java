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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Optional;

import org.hibernate.SessionFactory;
import org.hl7.fhir.r4.model.ContactPoint;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.LocationAttributeType;
import org.openmrs.PersonAttributeType;
import org.openmrs.ProviderAttributeType;
import org.openmrs.api.LocationService;
import org.openmrs.api.PersonService;
import org.openmrs.api.ProviderService;
import org.openmrs.module.fhir2.BaseFhirContextSensitiveTest;
import org.openmrs.module.fhir2.api.FhirContactPointMapService;
import org.openmrs.module.fhir2.model.FhirContactPointMap;
import org.springframework.beans.factory.annotation.Autowired;

public class FhirContactPointMapDaoImplTest extends BaseFhirContextSensitiveTest {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private LocationService locationService;
	
	@Autowired
	private ProviderService providerService;
	
	@Autowired
	private FhirContactPointMapService fhirContactPointMapService;
	
	private FhirContactPointMapDaoImpl fhirContactPointMapDao;
	
	private FhirContactPointMap fhirContactPointMap;
	
	private PersonAttributeType personAttributeType;
	
	private LocationAttributeType locationAttributeType;
	
	private ProviderAttributeType providerAttributeType;
	
	private static final String FHIR_CONTACT_POINT_INITIAL_DATA_XML = "org/openmrs/module/fhir2/api/dao/impl/FhirContactPointMapDaoImplTest_initial_data.xml";
	
	private static final String FHIR_CONTACT_POINT_MAP_UUID = "497daha3-5750-4yf6-8g11-518c49f73445";
	
	private static final String PERSON_ATTRIBUTE_TYPE_UUID = "a0f5521c-dbbd-4c10-81b2-1b7ab18330df";
	
	private static final String LOCATION_ATTRIBUTE_TYPE_UUID = "9516cc50-6f9f-11e0-8414-001e378eb67e";
	
	private static final String PROVIDER_ATTRIBUTE_TYPE_UUID = "257daea3-5750-4ff6-8d11-518c49f73556";
	
	@Before
	public void setup() throws Exception {
		fhirContactPointMapDao = new FhirContactPointMapDaoImpl();
		fhirContactPointMapDao.setSessionFactory(sessionFactory);
		personAttributeType = new PersonAttributeType();
		locationAttributeType = new LocationAttributeType();
		providerAttributeType = new ProviderAttributeType();
		
		fhirContactPointMap = new FhirContactPointMap();
		fhirContactPointMap.setUuid("497daha3-5750-4yf6-8g11-518c49f73445");
		fhirContactPointMap.setSystem(ContactPoint.ContactPointSystem.PHONE);
		fhirContactPointMap.setUse(ContactPoint.ContactPointUse.WORK);
		fhirContactPointMap.setRank(3);
		fhirContactPointMap.setAttributeTypeId(1);
		fhirContactPointMap.setAttributeTypeDomain("provider");
		fhirContactPointMapService.saveFhirContactPointMap(fhirContactPointMap);
		
		executeDataSet(FHIR_CONTACT_POINT_INITIAL_DATA_XML);
	}
	
	@Test
	public void getFhirContactPointMapByUuid_shouldGetFhirContactPointMapByUuid() {
		Optional<FhirContactPointMap> result = fhirContactPointMapDao
		        .getFhirContactPointMapByUuid(FHIR_CONTACT_POINT_MAP_UUID);
		
		assertThat(result.isPresent(), is(true));
		assertThat(result.get().getUuid(), is(FHIR_CONTACT_POINT_MAP_UUID));
		assertThat(result.get().getSystem(), is(ContactPoint.ContactPointSystem.PHONE));
		assertThat(result.get().getUse(), is(ContactPoint.ContactPointUse.WORK));
		assertThat(result.get().getRank(), is(3));
	}
	
	@Test
	public void getFhirContactPointMapForPersonAttributeType_shouldRetrieveContactPointMap() {
		personAttributeType = personService.getPersonAttributeTypeByName("telecom");
		assertThat(personAttributeType, notNullValue());
		assertThat(personAttributeType.getUuid(), equalTo(PERSON_ATTRIBUTE_TYPE_UUID));
		
		Optional<FhirContactPointMap> result = fhirContactPointMapDao
		        .getFhirContactPointMapForPersonAttributeType(personAttributeType);
		assertThat(result, notNullValue());
	}
	
	@Test
	public void getFhirContactPointMapForAttributeType_shouldRetrieveContactPointMapWhenLocationAttributeTypeIsFound() {
		locationAttributeType = locationService.getLocationAttributeTypeByName("telecom");
		assertThat(locationAttributeType, notNullValue());
		assertThat(locationAttributeType.getUuid(), equalTo(LOCATION_ATTRIBUTE_TYPE_UUID));
		
		Optional<FhirContactPointMap> result = fhirContactPointMapDao
		        .getFhirContactPointMapForAttributeType(locationAttributeType);
		assertThat(result, notNullValue());
	}
	
	@Test
	public void getFhirContactPointMapForAttributeType_shouldRetrieveContactPointMapWhenProviderAttributeTypeIsFound() {
		providerAttributeType = providerService.getProviderAttributeTypeByUuid(PROVIDER_ATTRIBUTE_TYPE_UUID);
		assertThat(providerAttributeType, notNullValue());
		assertThat(providerAttributeType.getUuid(), equalTo(PROVIDER_ATTRIBUTE_TYPE_UUID));
		
		Optional<FhirContactPointMap> result = fhirContactPointMapDao
		        .getFhirContactPointMapForAttributeType(providerAttributeType);
		assertThat(result, notNullValue());
	}
}
