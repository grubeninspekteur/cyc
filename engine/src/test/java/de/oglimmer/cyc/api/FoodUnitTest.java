package de.oglimmer.cyc.api;

import org.junit.Assert;
import org.junit.Test;

import de.oglimmer.cyc.api.Constants.Mode;
import de.oglimmer.cyc.util.CountMap;

public class FoodUnitTest {

	@Test
	public void testIncDay() {
		Game game = new Game(Mode.FULL);
		Company company = new Company(game, "companyA", game.getGrocer());
		Establishment est = new Establishment(company, "cityA", 5, 50, 1000, 2000);

		FoodUnit fu = new FoodUnit(Food.BEEF_MEAT, 10);
		for (int i = 0; i < 9; i++) {
			Assert.assertEquals(10 - i, fu.getPullDate());
			Assert.assertFalse(fu.incDay(est));
			Assert.assertEquals(9 - i, fu.getPullDate());
			Assert.assertEquals(10, fu.getUnits());
			Assert.assertNull(game.getGameRun().getResult().get("companyA").getTotalRottenFood()
					.get(Food.BEEF_MEAT.toString()));
		}
		Assert.assertTrue(fu.incDay(est));
		Assert.assertEquals(0, fu.getUnits());
		Assert.assertEquals(0, fu.getPullDate());
		Assert.assertEquals(10,
				game.getGameRun().getResult().get("companyA").getTotalRottenFood().get(Food.BEEF_MEAT.toString())
						.longValue());
	}

	@Test
	public void testSatisfyIngredient() {
		FoodUnitAdmin fua = new FoodUnitAdmin();

		CountMap<Food> mapUsed = new CountMap<>();

		fua.satisfyIngredient(mapUsed, Food.BEEF_MEAT);
		Assert.assertEquals(1, (long) mapUsed.get(Food.BEEF_MEAT));
		fua.satisfyIngredient(mapUsed, Food.BEEF_MEAT);
		Assert.assertEquals(2, (long) mapUsed.get(Food.BEEF_MEAT));
	}

	@Test
	public void testCheckIngredient() {
		FoodUnitAdmin fua = new FoodUnitAdmin();

		CountMap<Food> mapAvail = new CountMap<>();
		CountMap<Food> mapUsed = new CountMap<>();

		Assert.assertFalse(fua.checkIngredient(mapAvail, mapUsed, Food.BEEF_MEAT));

		mapAvail.put(Food.BEEF_MEAT, 1L);
		Assert.assertTrue(fua.checkIngredient(mapAvail, mapUsed, Food.BEEF_MEAT));

		mapAvail.put(Food.BEEF_MEAT, 0L);
		Assert.assertFalse(fua.checkIngredient(mapAvail, mapUsed, Food.BEEF_MEAT));

		mapAvail.put(Food.BEEF_MEAT, 1L);
		mapUsed.put(Food.BEEF_MEAT, 1L);
		Assert.assertFalse(fua.checkIngredient(mapAvail, mapUsed, Food.BEEF_MEAT));

		mapAvail.put(Food.BEEF_MEAT, 2L);
		mapUsed.put(Food.BEEF_MEAT, 1L);
		Assert.assertTrue(fua.checkIngredient(mapAvail, mapUsed, Food.BEEF_MEAT));
	}

	@Test
	public void testSplit() {
		FoodUnit fu = new FoodUnit(Food.BEEF_MEAT, 20);
		FoodUnit fuNew = fu.split(5);
		Assert.assertEquals(fu.getFood(), fuNew.getFood());
		Assert.assertEquals(fu.getPullDate(), fuNew.getPullDate());
		Assert.assertEquals(15, fu.getUnits());
		Assert.assertEquals(5, fuNew.getUnits());
	}

	@Test
	public void testSplitTooMuch() {
		FoodUnit fu = new FoodUnit(Food.BEEF_MEAT, 20);
		FoodUnit fuNew = fu.split(21);
		Assert.assertEquals(20, fu.getUnits());
		Assert.assertNull(fuNew);
	}

	@Test
	public void testSplitMax() {
		FoodUnit fu = new FoodUnit(Food.BEEF_MEAT, 20);
		FoodUnit fuNew = fu.split(20);
		Assert.assertEquals(20, fu.getUnits());
		Assert.assertNull(fuNew);
	}

	@Test
	public void testSplitZero() {
		FoodUnit fu = new FoodUnit(Food.BEEF_MEAT, 20);
		FoodUnit fuNew = fu.split(0);
		Assert.assertEquals(20, fu.getUnits());
		Assert.assertNull(fuNew);
	}
}
