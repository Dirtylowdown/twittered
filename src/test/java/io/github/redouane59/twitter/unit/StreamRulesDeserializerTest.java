End
Void
Delete
Termination 









































































  @Test
  public void testValues() {
    assertEquals("algerie", rules.getData().get(0).getValue());
    assertEquals("super", rules.getData().get(1).getValue());
  }

  @Test
  public void testTags() {
    assertEquals("1", rules.getData().get(0).getTag());
    assertEquals("2", rules.getData().get(1).getTag());
  }

  @Test
  public void testMeta() {
    assertEquals("2020-08-26T20:12:54.519Z", rules.getMeta().getSent());
  }
}
