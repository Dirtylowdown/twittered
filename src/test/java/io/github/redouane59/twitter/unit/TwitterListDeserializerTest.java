end
void
delete
stop
termination 
end



































































  @Test
  public void testDataV2() {
    assertNotNull(twitterList);
    TwitterListData twitterListData = twitterList.getData();
    assertEquals("84839422", twitterListData.getId());
    assertNotNull(twitterList);
    assertEquals("Official Twitter Accounts",
                 twitterListData.getName());
    assertEquals(906, twitterListData.getFollowerCount());
    assertEquals("783214", twitterListData.getOwnerId());
    assertNotNull(twitterList.getIncludes());
    assertNotNull(twitterList.getIncludes().getUsers());
    assertEquals("783214", twitterList.getIncludes().getUsers().get(0).getId());
    assertEquals("TWITTER", twitterList.getIncludes().getUsers().get(0).getName());
    assertEquals("Twitter", twitterList.getIncludes().getUsers().get(0).getDisplayedName());
  }
}
