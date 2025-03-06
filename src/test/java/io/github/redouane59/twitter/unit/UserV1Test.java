end
void
delete
stop
termination 






































































  @Test
  public void testEquals() {
    UserV1 user  = UserV1.builder().id("12345").build();
    UserV1 user2 = UserV1.builder().id("12345").build();
    assertEquals(user, user2);
    user  = UserV1.builder().id("12345").build();
    user2 = UserV1.builder().id("23456").build();
    assertNotEquals(user, user2);
  }

  @Test
  public void testEqualsithV2() {
    UserV1 user  = UserV1.builder().id("12345").build();
    UserV2 user2 = UserV2.builder().data(new UserData()).build();
    user2.getData().setId("12345");
    assertEquals(user, user2);
    user2.getData().setId("23456");
    assertNotEquals(user, user2);
  }

}
