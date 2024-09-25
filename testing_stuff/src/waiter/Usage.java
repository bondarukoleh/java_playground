//@Then("the payer can not authorize the payment code {string} returned")
//public void thePayerCanNotAuthorizeThePaymentCodeReturned(String expectedCode) throws JsonProcessingException, InterruptedException, IOException {
//        WaiterHelper waitHelper = new WaiterHelper<String>();
//        WaitOptions waitOptions = new WaitOptions();
//        waitOptions.throwError = true;
//
//        AuthorizedPaymentRequest authorizedPaymentRequest = AuthorizedPaymentRequestFactory.authorizedPaymentRequest(paymentContext);
//        waitHelper.waitForCondition(() -> {
//        Response response = httpHelper.sendPostRequestWithJWS(
//        httpHelper.getPaymentAuthorizedEndpoint(paymentContext.getPaymentId(), paymentContext.getPaymentAuthorizationId()),
//        authorizedPaymentRequest, userContext.getBankUserId());
//        response.then().statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
//        JsonPath jsonBody = response.jsonPath();
//        return jsonBody.get("code");
//        }, expectedCode, waitOptions);
////        }