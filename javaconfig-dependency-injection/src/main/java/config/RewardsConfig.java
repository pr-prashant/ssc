package config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import rewards.RewardNetwork;
import rewards.internal.RewardNetworkImpl;
import rewards.internal.account.AccountRepository;
import rewards.internal.account.JdbcAccountRepository;
import rewards.internal.restaurant.JdbcRestaurantRepository;
import rewards.internal.restaurant.RestaurantRepository;
import rewards.internal.reward.JdbcRewardRepository;
import rewards.internal.reward.RewardRepository;

import javax.sql.DataSource;

/**
 * RewardsConfig
 * <br>
 * <code>config.RewardsConfig</code>
 * <br>
 *
 * @author Prashant Patel
 * @since 30 Aug 2017
 */
public class RewardsConfig {

    @Autowired
    DataSource dataSource;

    @Bean
    public RewardNetwork rewardNetwork(){
        return new RewardNetworkImpl(
                accountRepository(),
                restaurantRepository(),
                rewardRepository());
    }

    @Bean
    public AccountRepository accountRepository(){
        JdbcAccountRepository jdbcAccountRepository = new JdbcAccountRepository();
        jdbcAccountRepository.setDataSource(dataSource);
        return jdbcAccountRepository;
    }

    @Bean
    public RestaurantRepository restaurantRepository(){
        JdbcRestaurantRepository jdbcRestaurantRepository = new JdbcRestaurantRepository();
        jdbcRestaurantRepository.setDataSource(dataSource);
        return jdbcRestaurantRepository;
    }

    @Bean
    public RewardRepository rewardRepository(){
        JdbcRewardRepository jdbcRewardRepository = new JdbcRewardRepository();
        jdbcRewardRepository.setDataSource(dataSource);
        return jdbcRewardRepository;
    }

}
