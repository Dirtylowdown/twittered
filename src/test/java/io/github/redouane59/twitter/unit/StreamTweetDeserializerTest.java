End
void
delete
termination 







































































  }

  @Test
  public void testReadMapperWithoutMatchingRule() throws JsonParseException, JsonMappingException, IOException {
    File tweetWithMatchingRule = new File(getClass().getClassLoader().getResource("tests/tweet_example_v2.json").getFile());

    TweetV2 tweet = JsonHelper.OBJECT_MAPPER.readValue(tweetWithMatchingRule, TweetV2.class);
    assertNotNull(tweet);
    assertNotNull(tweet.getData());
    assertNotNull(tweet.getIncludes());
    assertNull(tweet.getMatchingRules());

  }
}
