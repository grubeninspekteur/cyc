package de.oglimmer.cyc.api;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * Runs in a single thread.
 * 
 * @author oli
 *
 */
@Slf4j
public class Day {

	private Game game;
	private OpeningHours openingHours;
	private long dayStartTime;

	public Day(Game game) {
		this.game = game;
		this.openingHours = new OpeningHours(game);
	}

	void processDay(int day) {		
		clearCache();

		incDailyCounter(day);

		game.getGrocer().initDay();

		if ((day + 1) % 7 == 0) {
			callWeekly();
		}

		callDaily();

		deliverFood();

		openingHours.runBusiness();

		cleanFoodStorages();

	}

	private void clearCache() {
		for (Cache<?> c : game.getCaches()) {
			if (c.getType() == Cache.Type.DAILY) {
				c.reset();
			}
		}
	}

	private void incDailyCounter(int day) {
		game.setCurrentDay(game.getCurrentDay() + 1);
		log.info("Day: {}/{} ({}ms)", day, game.getCurrentDay(), System.currentTimeMillis()-dayStartTime);
		dayStartTime = System.currentTimeMillis();

		for (Company c : game.getCompanies()) {
			if (!c.isBankrupt()) {
				incDailyCounter(c);
			} else {
				log.debug("{} is bankrupt...", c.getName());
			}
		}
	}

	private void incDailyCounter(Company company) {
		company.incCash(0.0);
		PlayerResult playerResult = game.getResult().getCreateNotExists(company.getName());
		playerResult.addEstablishmentsByDays(company.getEstablishmentsInt().size());
		for (Employee e : company.getHumanResources().getEmployees()) {
			playerResult.addStaffByDays(e.getJobPosition().toString());
		}
		log.debug("{} at day {} => est={}, staff={} ", company.getName(), game.getCurrentDay(),
				game.getResult().getCreateNotExists(company.getName()).getEstablishmentsByDays(),
				game.getResult().getCreateNotExists(company.getName()).getStaffByDays());
	}

	private void callWeekly() {
		for (Company company : game.getCompanies()) {
			if (!company.isBankrupt()) {
				company.callWeekly();
			}
		}
	}

	private void callDaily() {
		game.getDailyStatisticsManager().reset();
		for (Company company : game.getCompanies()) {
			if (!company.isBankrupt()) {
				company.callDaily();
			}
		}
	}

	private void cleanFoodStorages() {
		for (Company c : game.getCompanies()) {
			// we want to run this for bankrupt companies as well, to show what food got rotten
			for (Establishment est : c.getEstablishmentsInt()) {
				est.cleanFoodStorage();
			}
		}
	}

	private void deliverFood() {
		Map<Company, FoodDelivery> foodDeliveries = game.getGrocer().createTodaysFoodDelivery();
		deliverFood(foodDeliveries);
		ThreadLocal.resetCompany();
	}

	private void deliverFood(Map<Company, FoodDelivery> foodDeliveries) {
		for (Company c : foodDeliveries.keySet()) {
			if (!c.isBankrupt()) {
				c.callFoodDelivery(foodDeliveries.get(c));
			}
		}
	}

	public void close() {
		openingHours.close();
	}
}
