package com.televisory.bond.dao;

import com.televisory.capitalmarket.entities.economy.CountryList;

public interface IBondJpa{
	
	CountryList findByCountryIsoCode2();

}
