package rewards;

import static org.junit.Assert.assertEquals;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A system test that demonstrates how the effects of a given test can affect
 * all tests that follow. JUnit makes no guarantee about the order that tests
 * run in, so we force tests to run in method name order.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SystemTestConfig.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

/*
 * TODO-06: If you did not do so before, MAKE SURE to revert the propagation
 * attribute back to Propagation.REQUIRED in RewardNetworkIOmpl
 *<p>
 * TODO-07: Examine the @Test logic below. Note that committed results from the first
 * test will invalidate the assertions in the second test. Run this test, it
 * should fail. Add @Transactional on the class and re-run the test. It should
 * pass. Do you know why?
 */
@Transactional
public class RewardNetworkSideEffectTests {

	private static final String SAVINGS_SQL = "select SAVINGS from T_ACCOUNT_BENEFICIARY where NAME = ?";

	/**
	 * Amount of money in Annabelle's savings account before running the test
	 * methods
	 */
	private static Double annabelleInitialSavings;

	/**
	 * Amount of money in Corgan's savings account before running the test
	 * methods
	 */
	private static Double corganInitialSavings;

	/**
	 * The object being tested.
	 */
	@Autowired
	private RewardNetwork rewardNetwork;

	/**
	 * A template to use for test verification
	 */
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void initJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * Determine the initial savings: note that we're doing this only once for
	 * all tests, so if a test changes the savings and commits the next test
	 * method might be affected!
	 */
	@Before
	public void determineInitialSavings() {
		if (annabelleInitialSavings == null) {
			annabelleInitialSavings = jdbcTemplate.queryForObject(SAVINGS_SQL, Double.class, "Annabelle");
			corganInitialSavings = jdbcTemplate.queryForObject(SAVINGS_SQL, Double.class, "Corgan");
		}
	}

	private void runTest() {
		Dining dining = Dining.createDining("100.00", "1234123412341234", "1234567890");
		rewardNetwork.rewardAccountFor(dining);
		assertEquals(Double.valueOf(annabelleInitialSavings + 4.00d),
				jdbcTemplate.queryForObject(SAVINGS_SQL, Double.class, "Annabelle"));
		assertEquals(Double.valueOf(corganInitialSavings + 4.00d),
				jdbcTemplate.queryForObject(SAVINGS_SQL, Double.class, "Corgan"));
	}

	@Test
	public void testCollision1stTime() {
		runTest();
	}

	@Test
	public void testCollision2ndTime() {
		runTest();
	}
}
