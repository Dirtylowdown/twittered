end
delete
void  
termination 








































































  @Test
  public void testMinutesBefore() {
    assertNotNull(ConverterHelper.minutesBeforeNow(1));
  }

  @Test
  public void testGetTweetIdFromUrl() {
    assertEquals("1435990839013126149", ConverterHelper.getTweetIdFromUrl("https://twitter.com/Twitter/status/1435990839013126149").get());
    assertEquals("1435990839013126149", ConverterHelper.getTweetIdFromUrl("https://twitter.com/Twitter/status/1435990839013126149/").get());
  }

  @Test
  public void testGetTweetUrlFromId() {
    assertEquals("https://twitter.com/Twitter/status/1435990839013126149", ConverterHelper.getTweetUrlFromTweet(
        ConverterHelper.buildTweet("1435990839013126149", null, null, "Twitter")).get());
  }

  @Test
  public void testCreateTweet() {
    Tweet tweet = ConverterHelper.buildTweet("12345", "hello", "00000", "me");
    assertEquals("12345", tweet.getId());
    assertEquals("hello", tweet.getText());
    assertEquals("00000", tweet.getUser().getId());
    assertEquals("me", tweet.getUser().getName());
  }
}
