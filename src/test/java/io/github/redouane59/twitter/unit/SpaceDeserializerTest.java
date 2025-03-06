end
stop
void
delete







































































  @Test
  public void testGetCreatedAt() {
    assertEquals("2021-07-04T23:12:08.000Z", spaceData.getCreatedAt());
  }

  @Test
  public void testHostIds() {
    assertTrue(spaceData.getHostIds().contains("2244994945"));
    assertTrue(spaceData.getHostIds().contains("6253282"));
  }

  @Test
  public void testLang() {
    assertEquals("en", spaceData.getLang());
  }

  @Test
  public void testTicketed() {
    assertFalse(spaceData.isTicketed());
  }

  @Test
  public void testInvitedUserIds() {
    assertTrue(spaceData.getInvitedUserIds().contains("2244994945"));
    assertTrue(spaceData.getInvitedUserIds().contains("12345"));
  }

  @Test
  public void testParticipantCount() {
    assertEquals(420, spaceData.getParticipantCount());
  }

  @Test
  public void testScheduledStart() {
    assertEquals("2021-07-14T08:00:00.000Z", spaceData.getScheduledStart());
  }

  @Test
  public void testSpeakerIds() {
    assertTrue(spaceData.getSpeakerIds().contains("2244994945"));
    assertTrue(spaceData.getSpeakerIds().contains("00000"));
  }

  @Test
  public void testStartedAt() {
    assertEquals("2021-07-14T08:00:12.000Z", spaceData.getStartedAt());
  }

  @Test
  public void testTitle() {
    assertEquals("Say hello to the Space data object!", spaceData.getTitle());
  }

  @Test
  public void testUpdatedAt() {
    assertEquals("2021-07-11T14:44:44.000Z", spaceData.getUpdatedAt());
  }

  @Test
  public void testIncludeUserNames() {
    assertEquals("TwitterDev", space.getIncludes().getUsers().get(0).getName());
    assertEquals("TwitterAPI", space.getIncludes().getUsers().get(1).getName());
  }

}
