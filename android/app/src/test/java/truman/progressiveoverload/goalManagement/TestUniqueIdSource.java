package truman.progressiveoverload.goalManagement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import truman.progressiveoverload.randomUtilities.RandomLong;
import truman.progressiveoverload.randomUtilities.RandomOther;

class TestUniqueIdSource {

    private Long rngResult_;
    private I_RandomLongGenerator rng_;
    private UniqueIdSource patient_;

    @BeforeEach
    public void resetEverything() {
        rngResult_ = new RandomLong().generate();
        rng_ = mock(I_RandomLongGenerator.class);
        when(rng_.randomLong()).thenReturn(rngResult_);
        patient_ = new UniqueIdSource(rng_);
    }

    @Test
    public void willRequestNumberFromRandomNumberGenerator() {
        patient_.nextAvailableId();

        verify(rng_, times(1)).randomLong();
    }

    @Test
    public void willReturnResultFromRandomNumberGeneratorIfIdNotAlreadyUsed() {
        assertEquals(rngResult_, patient_.nextAvailableId());
    }

    @Test
    public void willRegenerateRandomIdUntilUniqueIdFound() {
        RandomLong gen = new RandomLong();
        Long firstValidId = gen.generate();
        Long secondValidId = new RandomOther<>(gen).otherThan(firstValidId);
        when(rng_.randomLong())
                .thenReturn(firstValidId)
                .thenReturn(firstValidId)
                .thenReturn(firstValidId)
                .thenReturn(secondValidId);

        patient_.nextAvailableId();
        Long actualSecondId = patient_.nextAvailableId();

        assertEquals(secondValidId, actualSecondId);

    }

    @Test
    public void willReturnTrueAfterSuccessfullyReservingId() {
        assertTrue(patient_.attemptToReserveId(new RandomLong().generate()));
    }

    @Test
    public void willReturnFalseWhenReservingExistingId() {
        Long existingId = patient_.nextAvailableId();

        assertFalse(patient_.attemptToReserveId(existingId));
    }

    @Test
    public void cannotReserveSameIdMoreThanOnce() {
        Long idToReserve = new RandomLong().generate();
        patient_.attemptToReserveId(idToReserve);

        assertFalse(patient_.attemptToReserveId(idToReserve));
    }

    @Test
    public void willOnlyAllowOneThreadToReserveIdAtATime() {
        // Note: This test will not fail if there are printouts within the threaded section.
        // This test only fails intermittently in the red stage.  If this ever becomes flaky, it is a real problem.
        final int POOL_SIZE = 10;
        CountDownLatch startSignal = new CountDownLatch(POOL_SIZE);
        CountDownLatch doneSignal = new CountDownLatch(POOL_SIZE);
        Long idToFightOver = new RandomLong().generate();
        AtomicInteger reservationSuccessCounter = new AtomicInteger(0);
        class AttemptToReserveIdRunnable implements Runnable {
            private final int threadId_;

            public AttemptToReserveIdRunnable(int threadId) {
                threadId_ = threadId;
            }

            public void run() {
                try {
                    startSignal.countDown();
                    startSignal.await();
                    boolean success = patient_.attemptToReserveId(idToFightOver);
                    if (success) {
                        reservationSuccessCounter.getAndIncrement();
                    }
                } catch (InterruptedException ignore) {
                }
                doneSignal.countDown();
            }
        }
        ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE);

        for (int threadId = 0; threadId < POOL_SIZE; threadId++) {
            executor.submit(new AttemptToReserveIdRunnable(threadId));
        }

        try {
            doneSignal.await();
            assertEquals(1, reservationSuccessCounter.intValue());
        } catch (InterruptedException e) {
            fail("Encountered exception while waiting for threads to finish running");
        }

    }
}