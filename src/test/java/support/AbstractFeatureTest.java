package support;

import org.alfabet.exercise.Application;
import org.alfabet.exercise.integrations.processor.rest.ProcessorRestClient;
import org.alfabet.exercise.persistence.BillRepository;
import org.alfabet.exercise.persistence.RepaymentPlanRepository;
import org.alfabet.exercise.persistence.RepaymentRepository;
import org.alfabet.exercise.scheduling.PaymentScheduler;
import org.alfabet.exercise.services.billing.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    classes = Application.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
public abstract class AbstractFeatureTest {
    @Autowired
    public BillingService billingService;
    @Autowired
    public BillRepository billRepository;
    @Autowired
    public RepaymentPlanRepository repaymentPlanRepository;
    @Autowired
    public RepaymentRepository repaymentRepository;
    @Autowired
    public TestRestTemplate testRestTemplate;
    @MockBean
    public ProcessorRestClient processorRestClient;
    @SpyBean
    public PaymentScheduler paymentScheduler;
}
