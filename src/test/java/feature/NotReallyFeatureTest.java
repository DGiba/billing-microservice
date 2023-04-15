package feature;

import org.alfabet.exercise.api.processor.rest.Direction;
import org.alfabet.exercise.api.processor.rest.TransactionStatus;
import org.alfabet.exercise.domain.Bill;
import org.alfabet.exercise.domain.Repayment;
import org.alfabet.exercise.domain.RepaymentPlan;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import support.AbstractFeatureTest;

import java.time.Duration;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class NotReallyFeatureTest extends AbstractFeatureTest {

    @BeforeEach
    @AfterEach
    public void prepare() {
        deleteEntities();
    }

    @Test
    public void performAdvance() {
        Mockito.when(processorRestClient.performTransaction(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyDouble(),
                Mockito.any(Direction.class)))
            .thenReturn("TestTransactionId");

        testRestTemplate.getForEntity("/performAdvance?dstBankAccount=Test&amount=5000.50", Void.class);

        // One Bill created
        assertEquals(1, billRepository.findAll().size());
        var bill = billRepository.findAll().get(0);
        var repayments = repaymentRepository.findAll();
        repayments.forEach(repayment -> assertEquals(bill.getId(), repayment.getRepaymentPlan().getBill().getId()));

        // 12 Repayments created within a RepaymentPlan and their dates are valid
        assertEquals(12, repayments.size());
        assertDates(repayments);
    }

    @Test
    public void renew() {
        mockEntities();
        Mockito.when(processorRestClient.performTransaction(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyDouble(),
                Mockito.any(Direction.class)))
            .thenReturn("TestTransactionId");
        Mockito.when(processorRestClient.downloadReport())
            .thenReturn(Map.of("TestTransactionId", TransactionStatus.FAIL));

        //Makes sure that scheduler works once a minute in tests and added a new Repayment
        Awaitility.await().atMost(Duration.ofMinutes(1)).untilAsserted(paymentScheduler::pay);
        assertTrue(repaymentRepository.findAllByDate(LocalDate.now()).isEmpty());
        assertNotNull(repaymentRepository.findAllByDate(LocalDate.now().plusWeeks(12)).get(0));
    }

    private void deleteEntities() {
        repaymentRepository.deleteAll();
        repaymentPlanRepository.deleteAll();
        billRepository.deleteAll();
    }

    private void mockEntities() {
        var creditDate = LocalDate.now().minusWeeks(1);
        var bill = new Bill("TestBankAcc", 8000);
        var repaymentPlan = new RepaymentPlan(creditDate);
        var repayments = new LinkedList<Repayment>();

        bill.setCreditTransactionId("TestTransactionId");
        bill.setRepaymentPlan(repaymentPlan);
        repaymentPlan.setBill(bill);

        for (int i = 1; i <= 12; i++) {
            repayments.add(new Repayment(creditDate.plusWeeks(i), bill.getAmount() / 12, repaymentPlan));
        }
        repaymentPlan.setRepayments(repayments);

        billRepository.save(bill);
    }

    private void assertDates(List<Repayment> repayments) {
        for (int i = 0; i < 12; i++) {
            assertEquals(LocalDate.now().plusWeeks(i + 1), repayments.get(i).getDate());
        }
    }
}
