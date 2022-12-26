package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class SpeedTestService {

    private final Logger logger = LoggerFactory.getLogger(SpeedTestService.class);

    public long testStreamSpeed() {

        long startSingle = System.currentTimeMillis();
        long sum = Stream
                .iterate(1, a -> a + 1)
                .limit(1_000_000)
                .reduce(0, (a, b) -> a + b);
        long timeSingle = System.currentTimeMillis() - startSingle;
        logger.debug("Single stream time: {}",timeSingle);

        long startParallel = System.currentTimeMillis();
        sum = Stream
                .iterate(1, a -> a + 1)
                .limit(1_000_000)
                .parallel()
                .reduce(0, (a, b) -> a + b );
        long timeParallel = System.currentTimeMillis() - startParallel;
        logger.debug("Parallel stream time: {}",timeParallel);

        return sum;
    }
}
