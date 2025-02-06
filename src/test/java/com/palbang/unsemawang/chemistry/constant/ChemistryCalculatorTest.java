package com.palbang.unsemawang.chemistry.constant;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.palbang.unsemawang.chemistry.service.ChemistryCalculator;

class ChemistryCalculatorTest {

	@Test
	void testGoodDayStemMatch() {
		assertEquals(1, ChemistryCalculator.getChemistryScore("갑", "갑"));
		assertEquals(3, ChemistryCalculator.getChemistryScore("갑", "을"));
		assertEquals(6, ChemistryCalculator.getChemistryScore("갑", "병"));
		assertEquals(8, ChemistryCalculator.getChemistryScore("갑", "정"));
		assertEquals(-7, ChemistryCalculator.getChemistryScore("갑", "무"));
		assertEquals(6, ChemistryCalculator.getChemistryScore("갑", "기"));
		assertEquals(-7, ChemistryCalculator.getChemistryScore("갑", "경"));
		assertEquals(-5, ChemistryCalculator.getChemistryScore("갑", "신"));
		assertEquals(6, ChemistryCalculator.getChemistryScore("갑", "임"));
		assertEquals(8, ChemistryCalculator.getChemistryScore("갑", "계"));
	}

}