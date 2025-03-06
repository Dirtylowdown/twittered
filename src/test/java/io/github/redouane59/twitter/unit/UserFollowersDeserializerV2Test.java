end
void
delete
stop
termination 







































































  @Test
  public void testGetUsers() {
    assertNotNull(users);
    assertEquals(100, users.getData().size());
  }

  @Test
  public void testUserName() {
    Assertions.assertEquals("samsamia13", users.getData().get(0).getName());
  }

  @Test
  public void testUserId() {
    Assertions.assertEquals("606255425", users.getData().get(0).getId());
  }

  @Test
  public void testUserDescription() {
    Assertions.assertEquals("💉🩸 assistante du Ko .", users.getData().get(0).getDescription());
  }

  @Test
  public void testUserLocation() {
    Assertions.assertEquals("Paris, France", users.getData().get(0).getLocation());
  }

  @Test
  public void testUserFollowersCount() {
    Assertions.assertEquals(70, users.getData().get(0).getFollowersCount());
  }

  @Test
  public void testUserFollowingCount() {
    Assertions.assertEquals(515, users.getData().get(0).getFollowingCount());
  }

}



