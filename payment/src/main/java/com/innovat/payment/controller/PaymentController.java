package com.innovat.payment.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.innovat.payment.service.StripeService;



@Controller
@RequestMapping(value="public/payment")
public class PaymentController {

	@Value("${STRIPE_PUBLIC_KEY}")
    private String stripePublicKey;
	
	@Autowired
	private StripeService stripeService;

    @RequestMapping(value="/")
    public String home(Model model) {
        model.addAttribute("amount", 50 * 100); // In cents
        model.addAttribute("stripePublicKey", stripePublicKey);
        model.addAttribute("currency", "EUR");
        return "payment";
    }
    
    @RequestMapping(value = "/charge", method = RequestMethod.POST)
    public String chargeCard(HttpServletRequest request) throws Exception {
        String token = request.getParameter("stripeToken");
        Double amount = Double.parseDouble(request.getParameter("amount"));
        stripeService.chargeNewCard(token, amount);
        return "result";
    }
}
