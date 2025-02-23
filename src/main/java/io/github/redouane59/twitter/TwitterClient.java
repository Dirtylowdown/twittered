bEnd
Void
Delete
Close
Stop






































































































  @Override
  public TweetList getLikedTweets(final String userId, AdditionalParameters additionalParameters) {
    String              url        = getUrlHelper().getLikedTweetsUrl(userId);
    Map<String, String> parameters = new HashMap<>();
    parameters.put(TWEET_FIELDS, ALL_TWEET_FIELDS);
    if (!additionalParameters.isRecursiveCall()) {
      return getRequestHelper().getRequestWithParameters(url, parameters, TweetList.class).orElseThrow(NoSuchElementException::new);
    }
    if (additionalParameters.getMaxResults() <= 0) {
      parameters.put(MAX_RESULTS, String.valueOf(100));
    }
    return getTweetsRecursively(url, parameters, getRequestHelper());
  }

  @Override
  public TweetCountsList getTweetCounts(final String query) {
    return getTweetCounts(query, AdditionalParameters.builder().build());
  }

  @Override
  public TweetCountsList getTweetCounts(final String query, AdditionalParameters additionalParameters) {
    String url = getUrlHelper().getTweetsCountUrl();
    return getTweetCounts(url, query, additionalParameters);
  }

  @Override
  public TweetCountsList getAllTweetCounts(final String query) {
    return getAllTweetCounts(query, AdditionalParameters.builder().build());
  }

  @Override
  public TweetCountsList getAllTweetCounts(final String query, AdditionalParameters additionalParameters) {
    String url = urlHelper.getTweetsCountAllUrl();
    return getTweetCounts(url, query, additionalParameters);
  }

  private TweetCountsList getTweetCounts(String url, final String query, AdditionalParameters additionalParameters) {
    Map<String, String> parameters = additionalParameters.getMapFromParameters();
    parameters.put(QUERY, query);
    return getRequestHelperV2().getRequestWithParameters(url, parameters, TweetCountsList.class).orElseThrow(NoSuchElementException::new);
  }

  @SneakyThrows
  @Override
  public UserActionResponse muteUser(final String userId) {
    String url  = urlHelper.getMuteUserUrl(getUserIdFromAccessToken());
    String body = JsonHelper.toJson(new FollowBody(userId));
    return requestHelperV1.postRequestWithBodyJson(url, new HashMap<>(), body, UserActionResponse.class)
                          .orElseThrow(NoSuchElementException::new);
  }

  @Override
  public UserActionResponse unmuteUser(final String userId) {
    String url = urlHelper.getUnmuteUserUrl(getUserIdFromAccessToken(), userId);
    return requestHelperV1.makeRequest(Verb.DELETE, url, new HashMap<>(), null, true, UserActionResponse.class)
                          .orElseThrow(NoSuchElementException::new);
  }

  @Override
  public UserList getMutedUsers() {
    String              url        = urlHelper.getMutedUsersUrl(getUserIdFromAccessToken());
    Map<String, String> parameters = new HashMap<>();
    parameters.put(USER_FIELDS, ALL_USER_FIELDS);
    parameters.put(EXPANSION, PINNED_TWEET_ID);
    parameters.put(MAX_RESULTS, "1000");
    parameters.put(TWEET_FIELDS, ALL_TWEET_FIELDS);
    return requestHelperV1.getRequestWithParameters(url, parameters, UserList.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public RetweetResponse retweetTweet(String tweetId) {
    String url  = getUrlHelper().getRetweetTweetUrl(getUserIdFromAccessToken());
    String body = "{\"tweet_id\": \"" + tweetId + "\"}";
    return requestHelperV1.postRequestWithBodyJson(url, new HashMap<>(), body, RetweetResponse.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public RetweetResponse unretweetTweet(final String tweetId) {
    String url = getUrlHelper().getUnretweetTweetUrl(getUserIdFromAccessToken(), tweetId);
    return requestHelperV1.makeRequest(Verb.DELETE, url, new HashMap<>(), null, true, RetweetResponse.class)
                          .orElseThrow(NoSuchElementException::new);
  }

  @Override
  public Space getSpace(final String spaceId) {
    String              url        = getUrlHelper().getSpaceUrl(spaceId);
    Map<String, String> parameters = new HashMap<>();
    parameters.put(EXPANSION, "invited_user_ids,speaker_ids,creator_id,host_ids");
    parameters.put(SPACE_FIELDS, ALL_SPACE_FIELDS);
    parameters.put(USER_FIELDS, ALL_USER_FIELDS);
    return getRequestHelperV2().getRequestWithParameters(url, parameters, Space.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public SpaceList getSpaces(final List<String> spaceIds) {
    String              url        = getUrlHelper().getSpacesUrl();
    Map<String, String> parameters = new HashMap<>();
    parameters.put(EXPANSION, ALL_SPACE_EXPANSIONS);
    parameters.put(SPACE_FIELDS, ALL_SPACE_FIELDS);
    parameters.put(USER_FIELDS, ALL_USER_FIELDS);
    parameters.put("ids", String.join(", ", spaceIds));
    return getRequestHelperV2().getRequestWithParameters(url, parameters, SpaceList.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public SpaceList getSpacesByCreators(final List<String> creatorIds) {
    Map<String, String> parameters = new HashMap<>();
    parameters.put("user_ids", String.join(", ", creatorIds));
    parameters.put(EXPANSION, ALL_SPACE_EXPANSIONS);
    parameters.put(SPACE_FIELDS, ALL_SPACE_FIELDS);
    parameters.put(USER_FIELDS, ALL_USER_FIELDS);
    return getRequestHelperV2().getRequestWithParameters(getUrlHelper().getSpaceByCreatorUrl(), parameters, SpaceList.class)
                               .orElseThrow(NoSuchElementException::new);
  }

  @Override
  public SpaceList searchSpaces(final String query, final SpaceState state) {
    String              url        = getUrlHelper().getSearchSpacesUrl();
    Map<String, String> parameters = new HashMap<>();
    parameters.put("query", query);
    parameters.put("state", state.getLabel());
    parameters.put(EXPANSION, ALL_SPACE_EXPANSIONS);
    parameters.put(SPACE_FIELDS, ALL_SPACE_FIELDS);
    parameters.put(USER_FIELDS, ALL_USER_FIELDS);
    parameters.put(MAX_RESULTS, "100");
    return getRequestHelperV2().getRequestWithParameters(url, parameters, SpaceList.class)
                               .orElseThrow(NoSuchElementException::new);
  }

  @Override
  public UserList getSpaceBuyers(final String spaceId) {
    String              url        = getUrlHelper().getSpaceBuyersUrl(spaceId);
    Map<String, String> parameters = new HashMap<>();
    parameters.put(EXPANSION, PINNED_TWEET_ID);
    parameters.put(USER_FIELDS, ALL_USER_FIELDS);
    parameters.put(TWEET_FIELDS, ALL_TWEET_FIELDS);
    return getRequestHelperV2().getRequestWithParameters(url, parameters, UserList.class)
                               .orElseThrow(NoSuchElementException::new);
  }

  @SneakyThrows
  @Override
  public TwitterList createList(final String listName, final String description, final boolean isPrivate) {
    String          url  = getUrlHelper().getListUrlV2();
    TwitterListData body = TwitterListData.builder().name(listName).description(description).isPrivate(isPrivate).build();
    return getRequestHelperV1().postRequestWithBodyJson(url, null, JsonHelper.toJson(body), TwitterList.class)
                               .orElseThrow(NoSuchElementException::new);
  }

  @Override
  public boolean deleteList(final String listId) {
    String url = getUrlHelper().getListUrlV2() + "/" + listId;
    JsonNode jsonNode = getRequestHelperV1().makeRequest(Verb.DELETE, url, new HashMap<>(), null, true, JsonNode.class)
                                            .orElseThrow(NoSuchElementException::new);
    return jsonNode.get(DATA).get(DELETED).asBoolean();

  }

  @SneakyThrows
  @Override
  public boolean addListMember(final String listId, final String userId) {
    String                url  = getUrlHelper().getAddListMemberUrl(listId);
    TwitterListMemberData body = TwitterListMemberData.builder().userId(userId).build();
    JsonNode jsonNode =
        getRequestHelperV1().postRequestWithBodyJson(url, null, JsonHelper.toJson(body), JsonNode.class)
                            .orElseThrow(NoSuchElementException::new);
    return jsonNode.get(DATA).get(IS_MEMBER).asBoolean();
  }

  @Override
  public boolean removeListMember(final String listId, final String userId) {
    String url = getUrlHelper().getRemoveListMemberUrl(listId, userId);
    JsonNode jsonNode = getRequestHelperV1().makeRequest(Verb.DELETE, url, new HashMap<>(), null, true, JsonNode.class)
                                            .orElseThrow(NoSuchElementException::new);
    return jsonNode.get(DATA).get(IS_MEMBER).asBoolean();
  }

  @SneakyThrows
  @Override
  public boolean pinList(final String listId) {
    String url  = getUrlHelper().getPinListUrl(getUserIdFromAccessToken());
    String body = "{\"list_id\": \"" + listId + "\"}";
    JsonNode jsonNode = getRequestHelperV1().postRequestWithBodyJson(url, null, body, JsonNode.class)
                                            .orElseThrow(NoSuchElementException::new);
    return jsonNode.get(DATA).get(PINNED).asBoolean();
  }

  @Override
  public boolean unpinList(final String listId) {
    String url = getUrlHelper().getUnpinListUrl(getUserIdFromAccessToken(), listId);
    JsonNode jsonNode = getRequestHelperV1().makeRequest(Verb.DELETE, url, new HashMap<>(), null, true, JsonNode.class)
                                            .orElseThrow(NoSuchElementException::new);
    return jsonNode.get(DATA).get(PINNED).asBoolean();

  }

  @SneakyThrows
  @Override
  public boolean updateList(final String listId, final String listName, final String description, final boolean isPrivate) {
    String url = getUrlHelper().getListUrlV2() + "/" + listId;
    TwitterListData body = TwitterListData.builder()
                                          .name(listName).description(description).isPrivate(isPrivate).build();
    JsonNode jsonNode = getRequestHelperV1().makeRequest(Verb.PUT, url, new HashMap<>(), JsonHelper.toJson(body),
                                                         true, JsonNode.class).orElseThrow(NoSuchElementException::new);
    return jsonNode.get("updated").asBoolean();
  }

  @Override
  public boolean followList(final String listId) {
    String url  = getUrlHelper().getFollowListUrl(getUserIdFromAccessToken());
    String body = "{\"list_id\": \"" + listId + "\"}";
    JsonNode jsonNode = getRequestHelperV1().postRequestWithBodyJson(url, null, body, JsonNode.class)
                                            .orElseThrow(NoSuchElementException::new);
    return jsonNode.get(DATA).get(FOLLOWING).asBoolean();
  }

  @Override
  public boolean unfollowList(final String listId) {
    String url = getUrlHelper().getUnfollowListUrl(getUserIdFromAccessToken(), listId);
    JsonNode jsonNode = getRequestHelperV1().makeRequest(Verb.DELETE, url, new HashMap<>(), null,
                                                         true, JsonNode.class).orElseThrow(NoSuchElementException::new);
    return jsonNode.get(DATA).get(FOLLOWING).asBoolean();
  }

  @Override
  public TwitterList getList(final String listId) {
    String              url        = getUrlHelper().getListUrlV2() + "/" + listId;
    Map<String, String> parameters = new HashMap<>();
    parameters.put(EXPANSION, "owner_id");
    parameters.put(LIST_FIELDS, ALL_LIST_FIELDS);
    parameters.put(USER_FIELDS, ALL_USER_FIELDS);
    return getRequestHelperV1().getRequestWithParameters(url, parameters, TwitterList.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public UserList getListMembers(final String listId) {
    String              url        = getUrlHelper().getAddListMemberUrl(listId);
    Map<String, String> parameters = new HashMap<>();
    parameters.put(EXPANSION, PINNED_TWEET_ID);
    parameters.put(USER_FIELDS, ALL_USER_FIELDS);
    parameters.put(TWEET_FIELDS, ALL_TWEET_FIELDS);
    return getUsersRecursively(Integer.MAX_VALUE, url, parameters);
  }

  @Override
  public TwitterListList getUserOwnedLists(final String userId) {
    String              url        = getUrlHelper().getOwnedListUrl(userId);
    Map<String, String> parameters = new HashMap<>();
    parameters.put(EXPANSION, "owner_id");
    parameters.put(LIST_FIELDS, ALL_LIST_FIELDS);
    parameters.put(USER_FIELDS, ALL_USER_FIELDS);
    return getRequestHelperV1().getRequestWithParameters(url, parameters, TwitterListList.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public TweetList getListTweets(String listId, AdditionalParameters additionalParameters) {
    String              url        = getUrlHelper().getListTweetsUrl(listId);
    Map<String, String> parameters = additionalParameters.getMapFromParameters();
    parameters.put(EXPANSION, ALL_EXPANSIONS);
    parameters.put(TWEET_FIELDS, ALL_TWEET_FIELDS);
    parameters.put(USER_FIELDS, ALL_USER_FIELDS);
    parameters.put(MEDIA_FIELD, ALL_MEDIA_FIELDS);

    if (!additionalParameters.isRecursiveCall()) {
      return getRequestHelperV2().getRequestWithParameters(url, parameters, TweetList.class).orElseThrow(NoSuchElementException::new);
    }

    if (additionalParameters.getMaxResults() <= 0) {
      parameters.put(MAX_RESULTS, String.valueOf(100));
    }

    return getTweetsRecursively(url, parameters, getRequestHelper());
  }

  @Override
  public Tweet postTweet(final String text) {
    return postTweet(TweetParameters.builder().text(text).build());
  }

  @SneakyThrows
  @Override
  public Tweet postTweet(final TweetParameters tweetParameters) {
    String url  = getUrlHelper().getPostTweetUrl();
    String body = JsonHelper.toJson(tweetParameters);
    return getRequestHelperV1().postRequestWithBodyJson(url, new HashMap<>(), body, TweetV2.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public boolean deleteTweet(final String tweetId) {
    String url = getUrlHelper().getPostTweetUrl() + "/" + tweetId;
    JsonNode jsonNode = getRequestHelperV1().makeRequest(Verb.DELETE, url, new HashMap<>(), null, true, JsonNode.class)
                                            .orElseThrow(NoSuchElementException::new);
    return jsonNode.get(DATA).get(DELETED).asBoolean();
  }

  @Override
  public DirectMessage getDirectMessageEvents() {
    return getDirectMessageEvents(AdditionalParameters.builder().maxResults(100).build());
  }

  @Override
  public DirectMessage getDirectMessageEvents(final AdditionalParameters additionalParameters) {
    String              url        = getUrlHelper().getDmEventsUrl();
    Map<String, String> parameters = additionalParameters.getMapFromParameters();
    parameters.put(DM_FIELDS, ALL_DM_FIELDS);
    parameters.put(EXPANSION, ALL_DM_EXPANSIONS);
    parameters.put(TWEET_FIELDS, ALL_TWEET_FIELDS);
    parameters.put(USER_FIELDS, ALL_USER_FIELDS);
    parameters.put(MEDIA_FIELD, ALL_MEDIA_FIELDS);
    return getRequestHelperV1().getRequestWithParameters(url, parameters, DirectMessage.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public DirectMessage getDirectMessagesByConversation(String conversationId) {
    return getDirectMessagesByConversation(conversationId, AdditionalParameters.builder().maxResults(100).build());
  }

  @Override
  public DirectMessage getDirectMessagesByConversation(String conversationId, final AdditionalParameters additionalParameters) {
    String              url        = getUrlHelper().getDmLookupUrl(conversationId);
    Map<String, String> parameters = additionalParameters.getMapFromParameters();
    parameters.put(DM_FIELDS, ALL_DM_FIELDS);
    parameters.put(EXPANSION, ALL_DM_EXPANSIONS);
    parameters.put(TWEET_FIELDS, ALL_TWEET_FIELDS);
    parameters.put(USER_FIELDS, ALL_USER_FIELDS);
    parameters.put(MEDIA_FIELD, ALL_MEDIA_FIELDS);
    return getRequestHelperV1().getRequestWithParameters(url, parameters, DirectMessage.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public DirectMessage getDirectMessagesByUser(final String participantId) {
    return getDirectMessagesByUser(participantId, AdditionalParameters.builder().maxResults(100).build());
  }

  @Override
  public DirectMessage getDirectMessagesByUser(final String participantId, final AdditionalParameters additionalParameters) {
    String              url        = getUrlHelper().getDmUserLookupUrl(participantId);
    Map<String, String> parameters = additionalParameters.getMapFromParameters();
    parameters.put(DM_FIELDS, ALL_DM_FIELDS);
    parameters.put(EXPANSION, ALL_DM_EXPANSIONS);
    parameters.put(TWEET_FIELDS, ALL_TWEET_FIELDS);
    parameters.put(USER_FIELDS, ALL_USER_FIELDS);
    parameters.put(MEDIA_FIELD, ALL_MEDIA_FIELDS);
    return getRequestHelperV1().getRequestWithParameters(url, parameters, DirectMessage.class).orElseThrow(NoSuchElementException::new);

  }

  @Override
  public PostDmResponse createDirectMessage(final String conversationId, String text) {
    return createDirectMessage(conversationId, DmMessage.builder().text(text).build());
  }

  @Override
  public PostDmResponse createDirectMessage(final String conversationId, DmMessage message) {
    String url = getUrlHelper().getPostConversationDmUrl(conversationId);
    String body;
    try {
      body = JsonHelper.toJson(message);
    } catch (JsonProcessingException e) {
      LOGGER.error(e.getMessage(), e);
      throw new IllegalArgumentException();
    }
    return getRequestHelperV1().postRequestWithBodyJson(url, null, body, PostDmResponse.class).orElseThrow(NoSuchElementException::new);
  }

  public PostDmResponse createGroupDmConversation(List<String> participantIds, String text) {
    return createGroupDmConversation(DmParameters.builder()
                                                 .participantIds(participantIds)
                                                 .message(DmMessage.builder().text(text).build())
                                                 .build());
  }

  public PostDmResponse createGroupDmConversation(DmParameters parameters) {
    String url = getUrlHelper().getCreateDmConversationUrl();
    String body;
    try {
      body = JsonHelper.toJson(parameters);
    } catch (JsonProcessingException e) {
      LOGGER.error(e.getMessage(), e);
      throw new IllegalArgumentException();
    }
    return getRequestHelperV1().postRequestWithBodyJson(url, null, body, PostDmResponse.class).orElseThrow(NoSuchElementException::new);
  }

  public PostDmResponse createUserDmConversation(String participantId, String text) {
    return createUserDmConversation(participantId, DmMessage.builder().text(text).build());
  }

  public PostDmResponse createUserDmConversation(String participantId, DmMessage message) {
    String url = getUrlHelper().getPostUserDmUrl(participantId);
    String body;
    try {
      body = JsonHelper.toJson(message);
    } catch (JsonProcessingException e) {
      LOGGER.error(e.getMessage(), e);
      throw new IllegalArgumentException();
    }
    return getRequestHelperV1().postRequestWithBodyJson(url, null, body, PostDmResponse.class).orElseThrow(NoSuchElementException::new);
  }


  @Override
  public Tweet getTweet(String tweetId) {
    String              url        = getUrlHelper().getTweetUrl(tweetId);
    Map<String, String> parameters = new HashMap<>();
    parameters.put(EXPANSION, ALL_EXPANSIONS);
    parameters.put(TWEET_FIELDS, ALL_TWEET_FIELDS);
    parameters.put(USER_FIELDS, ALL_USER_FIELDS);
    parameters.put(MEDIA_FIELD, ALL_MEDIA_FIELDS);
    return getRequestHelper().getRequestWithParameters(url, parameters, TweetV2.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public TweetList getTweets(List<String> tweetIds) {
    String              url        = getUrlHelper().getTweetsUrl();
    Map<String, String> parameters = new HashMap<>();
    parameters.put(EXPANSION, ALL_EXPANSIONS);
    parameters.put(TWEET_FIELDS, ALL_TWEET_FIELDS);
    parameters.put(USER_FIELDS, ALL_USER_FIELDS);
    parameters.put(MEDIA_FIELD, ALL_MEDIA_FIELDS);
    StringBuilder result = new StringBuilder();
    int           i      = 0;
    while (i < tweetIds.size() && i < URLHelper.MAX_LOOKUP) {
      String id = tweetIds.get(i);
      result.append(id);
      result.append(",");
      i++;
    }
    result.delete(result.length() - 1, result.length());
    parameters.put("ids", result.toString());
    return getRequestHelper().getRequestWithParameters(url, parameters, TweetList.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public boolean hideReply(final String tweetId, final boolean hide) {
    String url = getUrlHelper().getHideReplyUrl(tweetId);
    try {
      String body = JsonHelper.toJson(new HiddenData(hide));
      HiddenResponse response = requestHelperV1.putRequest(url, body, HiddenResponse.class)
                                               .orElseThrow(NoSuchElementException::new);
      return response.getData().isHidden();
    } catch (JsonProcessingException e) {
      LOGGER.error(e.getMessage(), e);
      throw new IllegalArgumentException();
    }
  }

  @Override
  public TweetList searchTweets(String query) {
    return searchTweets(query, AdditionalParameters.builder().maxResults(100).build());
  }

  @Override
  public TweetList searchTweets(String query, AdditionalParameters additionalParameters) {
    Map<String, String> parameters = additionalParameters.getMapFromParameters();
    parameters.put(QUERY, query);
    parameters.put(TWEET_FIELDS, ALL_TWEET_FIELDS);
    parameters.put(USER_FIELDS, ALL_USER_FIELDS);
    parameters.put(EXPANSION, ALL_EXPANSIONS);
    parameters.put(MEDIA_FIELD, ALL_MEDIA_FIELDS);
    String url = urlHelper.getSearchRecentTweetsUrl();
    if (!additionalParameters.isRecursiveCall()) {
      return getRequestHelper().getRequestWithParameters(url, parameters, TweetList.class).orElseThrow(NoSuchElementException::new);
    }
    if (additionalParameters.getMaxResults() <= 0) {
      parameters.put(MAX_RESULTS, String.valueOf(100));
    }
    return getTweetsRecursively(url, parameters, getRequestHelper());
  }

  @Override
  public TweetList searchAllTweets(final String query) {
    return searchAllTweets(query, AdditionalParameters.builder().maxResults(500).build());
  }

  @Override
  public TweetList searchAllTweets(final String query, AdditionalParameters additionalParameters) {
    Map<String, String> parameters = additionalParameters.getMapFromParameters();
    parameters.put(QUERY, query);
    if (additionalParameters.getMaxResults() <= 100) {
      parameters.put(TWEET_FIELDS, ALL_TWEET_FIELDS);
    } else {
      LOGGER.warn("Removing context_annotations from tweet_fields because max_result is greater 100");
      parameters.put(TWEET_FIELDS, ALL_TWEET_FIELDS.replace(",context_annotations", ""));
    }
    parameters.put(USER_FIELDS, ALL_USER_FIELDS);
    parameters.put(EXPANSION, ALL_EXPANSIONS);
    parameters.put(MEDIA_FIELD, ALL_MEDIA_FIELDS);
    String url = urlHelper.getSearchAllTweetsUrl();
    if (!additionalParameters.isRecursiveCall()) {
      return getRequestHelperV2().getRequestWithParameters(url, parameters, TweetList.class).orElseThrow(NoSuchElementException::new);
    }
    if (additionalParameters.getMaxResults() <= 0) {
      parameters.put(MAX_RESULTS, String.valueOf(100));
    }
    return getTweetsRecursively(url, parameters, getRequestHelperV2());
  }

  /**
   * Call an endpoint related to tweets recursively until next_token is null to provide a full result
   */
  private TweetList getTweetsRecursively(String url, Map<String, String> parameters, AbstractRequestHelper requestHelper) {
    String    next;
    TweetList result   = TweetList.builder().data(new ArrayList<>()).meta(new TweetMeta()).build();
    String    newestId = null;
    do {
      Optional<TweetList> tweetList = requestHelper.getRequestWithParameters(url, parameters, TweetList.class);
      if (!tweetList.isPresent() || tweetList.get().getData() == null) {
        result.getMeta().setNextToken(null);
        break;
      }
      result.getData().addAll(tweetList.get().getData());
      if (newestId == null) {
        newestId = tweetList.get().getMeta().getNewestId();
      }
      TweetMeta meta = TweetMeta.builder()
                                .resultCount(result.getData().size())
                                .oldestId(tweetList.get().getMeta().getOldestId())
                                .newestId(newestId)
                                .nextToken(tweetList.get().getMeta().getNextToken())
                                .build();
      result.setMeta(meta);
      result.setIncludes(tweetList.get().getIncludes());
      next = tweetList.get().getMeta().getNextToken();
      if (url.contains("/search")) { // dirty
        parameters.put(AdditionalParameters.NEXT_TOKEN, next);
      } else {
        parameters.put(AdditionalParameters.PAGINATION_TOKEN, next);
      }
    } while (next != null);
    return result;
  }

  /**
   * Call an endpoint related to users recursively until next_token is null to provide a full result
   */
  private UserList getUsersRecursively(String url, Map<String, String> parameters, AbstractRequestHelper requestHelper) {
    String   next;
    UserList result = UserList.builder().data(new ArrayList<>()).meta(new UserMeta()).build();
    do {
      Optional<UserList> userList = requestHelper.getRequestWithParameters(url, parameters, UserList.class);
      if (!userList.isPresent() || userList.get().getData() == null) {
        result.getMeta().setNextToken(null);
        break;
      }
      result.getData().addAll(userList.get().getData());
      UserMeta meta = UserMeta.builder()
                              .resultCount(result.getData().size())
                              .nextToken(userList.get().getMeta().getNextToken())
                              .build();
      result.setMeta(meta);
      next = userList.get().getMeta().getNextToken();
      parameters.put(AdditionalParameters.PAGINATION_TOKEN, next);
    } while (next != null);
    return result;
  }

  @Deprecated
  @Override
  /**
   * Use {@link TwitterClient#searchTweets(query)} instead.
   */
  public List<Tweet> searchForTweetsWithin30days(String query, LocalDateTime fromDate, LocalDateTime toDate,
                                                 String envName) {
    int                 count      = 100;
    Map<String, String> parameters = new HashMap<>();
    parameters.put(QUERY, query);
    parameters.put("maxResults", String.valueOf(count));
    parameters.put("fromDate", ConverterHelper.getStringFromDate(fromDate));
    parameters.put("toDate", ConverterHelper.getStringFromDate(toDate));
    String      next;
    List<Tweet> result = new ArrayList<>();
    do {
      Optional<TweetSearchResponseV1> tweetSearchV1DTO = getRequestHelper().getRequestWithParameters(
          urlHelper.getSearchTweet30DaysUrl(envName), parameters, TweetSearchResponseV1.class);
      if (!tweetSearchV1DTO.isPresent() || tweetSearchV1DTO.get().getResults() == null) {
        break;
      }
      result.addAll(tweetSearchV1DTO.get().getResults());
      next = tweetSearchV1DTO.get().getNext();
      parameters.put(NEXT, next);
    } while (next != null);
    return result;
  }

  @Override
  @Deprecated
  /**
   * Use {@link TwitterClient#searchAllTweets(String)} (query)} instead.
   */
  public List<Tweet> searchForTweetsArchive(String query, LocalDateTime fromDate, LocalDateTime toDate,
                                            String envName) {
    int                 count      = 100;
    Map<String, String> parameters = new HashMap<>();
    parameters.put(QUERY, query);
    parameters.put(MAX_RESULTS, String.valueOf(count));
    parameters.put("fromDate", ConverterHelper.getStringFromDate(fromDate));
    parameters.put("toDate", ConverterHelper.getStringFromDate(toDate));
    String      next;
    List<Tweet> result = new ArrayList<>();
    do {
      Optional<TweetSearchResponseV1> tweetSearchV1DTO = getRequestHelper().getRequestWithParameters(
          urlHelper.getSearchTweetFullArchiveUrl(envName), parameters, TweetSearchResponseV1.class);
      if (!tweetSearchV1DTO.isPresent()) {
        LOGGER.error("Empty response on searchForTweetsArchive");
        break;
      }
      result.addAll(tweetSearchV1DTO.get().getResults());
      next = tweetSearchV1DTO.get().getNext();
      parameters.put(NEXT, next);
    } while (next != null);
    return result;
  }

  @Override
  public Future<Response> startFilteredStream(Consumer<Tweet> consumer) {
    String              url        = urlHelper.getFilteredStreamUrl();
    Map<String, String> parameters = new HashMap<>();
    parameters.put(EXPANSION, ALL_EXPANSIONS);
    parameters.put(TWEET_FIELDS, ALL_TWEET_FIELDS);
    parameters.put(USER_FIELDS, ALL_USER_FIELDS);
    parameters.put(MEDIA_FIELD, ALL_MEDIA_FIELDS);
    return requestHelperV2.getAsyncRequest(url, parameters, consumer);
  }

  @Override
  public Future<Response> startFilteredStream(IAPIEventListener listener) {
    return startFilteredStream(listener, 0);
  }

  @Override
  public Future<Response> startFilteredStream(IAPIEventListener listener, int backfillMinutes) {
    String              url        = urlHelper.getFilteredStreamUrl();
    Map<String, String> parameters = new HashMap<>();
    parameters.put(EXPANSION, ALL_EXPANSIONS);
    parameters.put(TWEET_FIELDS, ALL_TWEET_FIELDS);
    parameters.put(USER_FIELDS, ALL_USER_FIELDS);
    parameters.put(MEDIA_FIELD, ALL_MEDIA_FIELDS);
    if (backfillMinutes > 0) {
      parameters.put(BACKFILL_MINUTES, String.valueOf(backfillMinutes));
    }
    return requestHelperV2.getAsyncRequest(url, parameters, listener);
  }

  @Override
  public boolean stopFilteredStream(Future<Response> responseFuture, long timeout, TimeUnit unit) {
    try {
      Response response;
      if (timeout > 0 && unit != null) {
        response = responseFuture.get(timeout, unit);
      } else {
        response = responseFuture.get();
      }

      if (response == null) {
        return false;
      }
      response.getStream().close();
      return true;
    } catch (IOException | InterruptedException | ExecutionException | TimeoutException e) {
      LOGGER.error("Couldn't stopFilteredstream ", e);
      Thread.currentThread().interrupt();
    }
    return false;
  }

  @Override
  public boolean stopFilteredStream(Future<Response> responseFuture) {
    return stopFilteredStream(responseFuture, 0, null);
  }

  @Override
  public List<StreamRule> retrieveFilteredStreamRules() {
    String      url    = urlHelper.getFilteredStreamRulesUrl();
    StreamRules result = requestHelperV2.getRequest(url, StreamRules.class).orElseThrow(NoSuchElementException::new);
    return result.getData();
  }

  @Override
  public StreamRule addFilteredStreamRule(String value, String tag) {
    String     url  = urlHelper.getFilteredStreamRulesUrl();
    StreamRule rule = StreamRule.builder().value(value).tag(tag).build();
    try {
      String      body   = "{\"add\": [" + JsonHelper.toJson(rule) + "]}";
      StreamRules result = requestHelperV2.postRequest(url, body, StreamRules.class).orElseThrow(NoSuchElementException::new);
      if (result.getData() == null || result.getData().isEmpty()) {
        LOGGER.error("Could not add filtered stream rule. Rule maybe already exists.");
        throw new IllegalArgumentException();
      }
      return result.getData().get(0);
    } catch (JsonProcessingException e) {
      LOGGER.error(e.getMessage(), e);
      throw new IllegalArgumentException();
    }
  }

  @Override
  public StreamRule addFilteredStreamRule(FilteredStreamRulePredicate value, String tag) {
    return addFilteredStreamRule(value.toString(), tag);
  }

  @Override
  public StreamMeta deleteFilteredStreamRule(FilteredStreamRulePredicate ruleValue) {
    return deleteFilteredStreamRule(ruleValue.toString());
  }

  @Override
  public StreamMeta deleteFilteredStreamRule(String ruleValue) {
    String      url    = urlHelper.getFilteredStreamRulesUrl();
    String      body   = "{\"delete\": {\"values\": [\"" + ruleValue + "\"]}}";
    StreamRules result = requestHelperV2.postRequest(url, body, StreamRules.class).orElseThrow(NoSuchElementException::new);
    return result.getMeta();
  }

  @Override
  public StreamMeta deleteFilteredStreamRuleId(String ruleId) {
    String      url    = urlHelper.getFilteredStreamRulesUrl();
    String      body   = "{\"delete\": {\"ids\": [\"" + ruleId + "\"]}}";
    StreamRules result = requestHelperV2.postRequest(url, body, StreamRules.class).orElseThrow(NoSuchElementException::new);
    return result.getMeta();
  }

  @Override
  public Future<Response> startSampledStream(Consumer<Tweet> consumer) {
    String              url        = urlHelper.getSampledStreamUrl();
    Map<String, String> parameters = new HashMap<>();
    parameters.put(EXPANSION, ALL_EXPANSIONS);
    parameters.put(TWEET_FIELDS, ALL_TWEET_FIELDS);
    parameters.put(USER_FIELDS, ALL_USER_FIELDS);
    parameters.put(MEDIA_FIELD, ALL_MEDIA_FIELDS);
    return requestHelperV2.getAsyncRequest(url, parameters, consumer);
  }

  @Override
  public Future<Response> startSampledStream(IAPIEventListener listener) {
    return startSampledStream(listener, 0);
  }

  @Override
  public Future<Response> startSampledStream(IAPIEventListener listener, int backfillMinutes) {
    String              url        = urlHelper.getSampledStreamUrl();
    Map<String, String> parameters = new HashMap<>();
    parameters.put(EXPANSION, ALL_EXPANSIONS);
    parameters.put(TWEET_FIELDS, ALL_TWEET_FIELDS);
    parameters.put(USER_FIELDS, ALL_USER_FIELDS);
    parameters.put(MEDIA_FIELD, ALL_MEDIA_FIELDS);
    if (backfillMinutes > 0) {
      parameters.put(BACKFILL_MINUTES, String.valueOf(backfillMinutes));
    }
    return requestHelperV2.getAsyncRequest(url, parameters, listener);
  }

  @Override
  public TweetList getUserTimeline(final String userId) {
    return getUserTimeline(userId, AdditionalParameters.builder().maxResults(100).build());
  }

  @Override
  public TweetList getUserTimeline(String userId, AdditionalParameters additionalParameters) {
    Map<String, String> parameters = additionalParameters.getMapFromParameters();
    parameters.put(TWEET_FIELDS, ALL_TWEET_FIELDS);
    parameters.put(USER_FIELDS, ALL_USER_FIELDS);
    parameters.put(PLACE_FIELDS, ALL_PLACE_FIELDS);
    parameters.put(POLL_FIELDS, ALL_POLL_FIELDS);
    parameters.put(MEDIA_FIELD, ALL_MEDIA_FIELDS);
    parameters.put(EXPANSION, ALL_EXPANSIONS);
    String url = urlHelper.getUserTimelineUrl(userId);
    if (!additionalParameters.isRecursiveCall()) {
      return getRequestHelperV2().getRequestWithParameters(url, parameters, TweetList.class).orElseThrow(NoSuchElementException::new);
    }
    if (additionalParameters.getMaxResults() <= 0) {
      parameters.put(MAX_RESULTS, String.valueOf(100));
    }
    return getTweetsRecursively(url, parameters, getRequestHelperV2());
  }

  @Override
  public TweetList getUserMentions(final String userId) {
    return getUserMentions(userId, AdditionalParameters.builder().maxResults(100).build());
  }

  @Override
  public TweetList getUserMentions(final String userId, AdditionalParameters additionalParameters) {
    Map<String, String> parameters = additionalParameters.getMapFromParameters();
    parameters.put(TWEET_FIELDS, ALL_TWEET_FIELDS);
    String url = urlHelper.getUserMentionsUrl(userId);
    if (!additionalParameters.isRecursiveCall()) {
      return getRequestHelperV2().getRequestWithParameters(url, parameters, TweetList.class).orElseThrow(NoSuchElementException::new);
    }
    if (additionalParameters.getMaxResults() <= 0) {
      parameters.put(MAX_RESULTS, String.valueOf(100));
    }
    return getTweetsRecursively(url, parameters, getRequestHelperV2());
  }

  @Override
  public List<TweetV1> readTwitterDataFile(File file) throws IOException {
    SimpleModule module = new SimpleModule();
    module.addDeserializer(TweetV1.class, new TweetV1Deserializer());
    ObjectMapper customObjectMapper = new ObjectMapper();
    customObjectMapper.registerModule(module);
    customObjectMapper.findAndRegisterModules();

    List<TweetV1> result = new ArrayList<>();
    if (!file.exists()) {
      LOGGER.error("File not found at : {}", file.toURI());
    } else {
      result = Arrays.asList(customObjectMapper.readValue(file, TweetV1[].class));
    }
    return result;
  }

  @Override
  public String getBearerToken() {
    return requestHelperV2.getBearerToken();
  }

  @Override
  public BearerToken getOAuth2RefreshToken(String refreshToken, String clientId) {
    String              url     = URLHelper.ACCESS_TOKEN_URL;
    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
    Map<String, String> params = new HashMap<>();
    params.put("refresh_token", refreshToken);
    params.put("client_id", clientId);
    params.put("grant_type", "refresh_token");
    return requestHelperV2.makeRequest(Verb.POST, url, headers, params, null, false, BearerToken.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public BearerToken getOAuth2AccessToken(String clientId, String code, String codeVerifier, String redirectUri) {
    String              url    = URLHelper.ACCESS_TOKEN_URL;
    Map<String, String> params = new HashMap<>();
    params.put("client_id", clientId);
    params.put("code", code);
    params.put("redirect_uri", redirectUri);
    params.put("code_verifier", codeVerifier);
    params.put("grant_type", "authorization_code");
    return requestHelperV2.makeRequest(Verb.POST, url, null, params, null, false, BearerToken.class).orElseThrow(NoSuchElementException::new);
  }

  @Override

  public RequestToken getOauth1Token() {
    return getOauth1Token(null);
  }

  @Override
  public RequestToken getOauth1Token(String oauthCallback) {
    String              url        = URLHelper.GET_OAUTH1_TOKEN_URL;
    Map<String, String> parameters = new HashMap<>();
    if (oauthCallback != null) {
      parameters.put("oauth_callback", oauthCallback);
    }
    String       stringResponse = requestHelperV1.postRequest(url, parameters, String.class).orElseThrow(NoSuchElementException::new);
    RequestToken requestToken   = new RequestToken(stringResponse);
    LOGGER.info("Open the following URL to grant access to your account:");
    LOGGER.info("https://twitter.com/oauth/authenticate?oauth_token={}", requestToken.getOauthToken());
    return requestToken;
  }

  @Override
  public RequestToken getOAuth1AccessToken(RequestToken requestToken, String pinCode) {
    String              url        = URLHelper.GET_OAUTH1_ACCESS_TOKEN_URL;
    Map<String, String> parameters = new HashMap<>();
    parameters.put("oauth_verifier", pinCode);
    parameters.put("oauth_token", requestToken.getOauthToken());
    String stringResponse = requestHelperV1.postRequestWithoutSign(url, parameters, String.class).orElseThrow(NoSuchElementException::new);
    return new RequestToken(stringResponse);
  }

  @Override
  public UploadMediaResponse uploadMedia(String mediaName, byte[] data, MediaCategory mediaCategory) {
    String url = urlHelper.getUploadMediaUrl(mediaCategory);
    return requestHelperV1.uploadMedia(url, mediaName, data, UploadMediaResponse.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public UploadMediaResponse uploadMedia(File imageFile, MediaCategory mediaCategory) {
    String url = urlHelper.getUploadMediaUrl(mediaCategory);
    return requestHelperV1.uploadMedia(url, imageFile, UploadMediaResponse.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public Optional<UploadMediaResponse> uploadChunkedMedia(String mediaName, long size, InputStream data, MediaCategory mediaCategory) {
    try {
      String type = URLConnection.guessContentTypeFromName(mediaName);
      String url = urlHelper.getChunkedUploadMediaUrl();
      Map<String, String> parameters = new HashMap<>();
      parameters.put("command", "INIT");
      parameters.put("total_bytes", Long.toString(size));
      parameters.put("media_type", type);
      parameters.put("medai_category", mediaCategory.label);
      UploadMediaResponse initRsp = requestHelperV1.postRequest(url, parameters, UploadMediaResponse.class).orElseThrow(NoSuchElementException::new);

      parameters.clear();
      parameters.put("command", "APPEND");
      parameters.put("media_id", initRsp.getMediaId());

      byte[] buf = new byte[(int) Math.min(size, 5 * 1024 * 1024L)]; // 5MB max chunk size
      int segmentIndex = 0;
      int count;
      try {
        while ((count = data.read(buf)) > 0) {
          parameters.put("segment_index", Integer.toString(segmentIndex++));
          requestHelperV1.uploadChunkedMedia(url, parameters, buf, 0, count, Void.class);
        }
      } catch (IOException ex) {
        LOGGER.error("Error occupied on reading media", ex);
        return Optional.empty();
      }

      parameters.clear();
      parameters.put("command", "FINALIZE");
      parameters.put("media_id", initRsp.getMediaId());

      UploadMediaResponse rsp = requestHelperV1.postRequest(url, parameters, UploadMediaResponse.class).orElseThrow(NoSuchElementException::new);
      UploadMediaProcessingInfo processing;
      while ((processing = rsp.getProcessingInfo()) != null && processing.getState().equals("pending")) {
        try {
          Thread.sleep(processing.getCheckAfterSecs() * 1000L);
        } catch (InterruptedException ex) {
          LOGGER.error("Error occupied on waiting media processing", ex);
        }

        parameters.clear();
        parameters.put("command", "STATUS");
        parameters.put("media_id", initRsp.getMediaId());

        rsp = requestHelperV1.getRequestWithParameters(url, parameters, UploadMediaResponse.class).orElseThrow(NoSuchElementException::new);
      }

      return Optional.of(rsp);
    } finally {
      try {
        data.close();
      } catch (IOException ex) {
        LOGGER.error("Error occupied on closing media stream", ex);
      }
    }
  }

  @Override
  public Optional<UploadMediaResponse> uploadChunkedMedia(File imageFile, MediaCategory mediaCategory) {
    try {
      return uploadChunkedMedia(imageFile.getName(), imageFile.length(), Files.newInputStream(imageFile.toPath()), mediaCategory);
    } catch (IOException ex) {
      LOGGER.error("Error occupied on reading media", ex);
      return Optional.empty();
    }
  }

  @Override
  public CollectionsResponse collectionsCreate(String name, String description, String collectionUrl, TimeLineOrder timeLineOrder) {
    String              url        = getUrlHelper().getCollectionsCreateUrl();
    Map<String, String> parameters = new HashMap<>();
    parameters.put("name", name);
    parameters.put("description", description);
    parameters.put("url", collectionUrl);
    if (timeLineOrder != null) {
      parameters.put("timeline_order", timeLineOrder.value());
    }
    return requestHelperV1.postRequest(url, parameters, CollectionsResponse.class).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public CollectionsResponse collectionsCurate(String collectionId, List<String> tweetIds) {
    String url = getUrlHelper().getCollectionsCurateUrl();

    // Can only curate 100 tweets at a time - so chunk if tweetIds is larger
    AtomicInteger index = new AtomicInteger(0);
    Stream<List<String>> chunked = tweetIds.stream()
                                           .collect(Collectors.groupingBy(x -> index.getAndIncrement() / URLHelper.MAX_LOOKUP))
                                           .entrySet().stream()
                                           .sorted(Map.Entry.comparingByKey())
                                           .map(Map.Entry::getValue);
    return chunked
        .map(
            chunk -> {
              String json = String.format("{\"id\": \"%s\",\"changes\": [", collectionId);
              json += chunk
                  .stream()
                  .map(tweetId -> String.format("{ \"op\": \"add\", \"tweet_id\": \"%s\"}", tweetId))
                  .collect(Collectors.joining(", "));
              json += "]}";
              return requestHelperV1.postRequestWithBodyJson(url, Collections.emptyMap(), json, CollectionsResponse.class)
                                    .orElseThrow(NoSuchElementException::new);
            })
        .filter(CollectionsResponse::hasErrors) // any errors? If so return first chunk of errors
        .findFirst()
        .orElse(new CollectionsResponse()); // success - no errors
  }

  @Override
  public CollectionsResponse collectionsDestroy(String collectionId) {
    String url = getUrlHelper().getCollectionsDestroyUrl(collectionId);
    return requestHelperV1.postRequest(url, Collections.emptyMap(), CollectionsResponse.class).orElseThrow(NoSuchElementException::new);
  }

  @Deprecated
  @Override
  public List<io.github.redouane59.twitter.dto.dm.deprecatedV1.DirectMessage> getDmList() {
    return getDmList(Integer.MAX_VALUE);
  }

  @Override
  public List<io.github.redouane59.twitter.dto.dm.deprecatedV1.DirectMessage> getDmList(int count) {
    List<io.github.redouane59.twitter.dto.dm.deprecatedV1.DirectMessage> result   = new ArrayList<>();
    int                                                                  maxCount = 50;
    String                                                               url      = getUrlHelper().getDMListUrl(maxCount);
    DmListAnswer                                                         dmListAnswer;
    do {
      dmListAnswer = requestHelperV1.getRequest(url, DmListAnswer.class).orElseThrow(NoSuchElementException::new);
      result.addAll(dmListAnswer.getDirectMessages());
      url = getUrlHelper().getDMListUrl(maxCount) + "&" + CURSOR + "=" + dmListAnswer.getNextCursor();
    }
    while (dmListAnswer.getNextCursor() != null && result.size() < count);
    return result.subList(0, Math.min(count, result.size())); // to fix the API bug which is not giving the right count
  }

  @Override
  public io.github.redouane59.twitter.dto.dm.deprecatedV1.DirectMessage getDm(String dmId) {
    String url = urlHelper.getDmUrl(dmId);
    io.github.redouane59.twitter.dto.dm.deprecatedV1.DmEvent
        result =
        getRequestHelper().getRequest(url, io.github.redouane59.twitter.dto.dm.deprecatedV1.DmEvent.class).orElseThrow(NoSuchElementException::new);
    return result.getEvent();
  }

  @Override
  public io.github.redouane59.twitter.dto.dm.deprecatedV1.DmEvent postDm(final String text, final String userId) {
    String url = urlHelper.getPostConversationDmUrl();
    try {
      String body = JsonHelper.toJson(
          io.github.redouane59.twitter.dto.dm.deprecatedV1.DmEvent.builder()
                                                                  .event(new io.github.redouane59.twitter.dto.dm.deprecatedV1.DirectMessage(text,
                                                                                                                                            userId))
                                                                  .build());
      return getRequestHelperV1().postRequestWithBodyJson(url, null, body, io.github.redouane59.twitter.dto.dm.deprecatedV1.DmEvent.class)
                                 .orElseThrow(NoSuchElementException::new);
    } catch (JsonProcessingException e) {
      LOGGER.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public CollectionsResponse collectionsEntries(final String collectionId, int count, String maxPosition, String minPosition) {
    String              url        = getUrlHelper().getCollectionsEntriesUrl(collectionId);
    Map<String, String> parameters = new HashMap<>();
    if (count > 0) {
      parameters.put("count", Integer.toString(count));
    }
    if (maxPosition != null) {
      parameters.put("max_position", maxPosition);
    }
    if (minPosition != null) {
      parameters.put("min_position", minPosition);
    }
    return getRequestHelper().getRequestWithParameters(url, parameters, CollectionsResponse.class).orElseThrow(NoSuchElementException::new);
  }

  private AbstractRequestHelper getRequestHelper() {
    if (requestHelperV1.getTwitterCredentials().getAccessToken() != null
        && requestHelperV1.getTwitterCredentials().getAccessTokenSecret() != null) {
      return requestHelperV1;
    } else {
      return requestHelperV2;
    }
  }

  public String getUserIdFromAccessToken() {
    String accessToken = twitterCredentials.getAccessToken();
    if (accessToken == null
        || accessToken.isEmpty()
        || !accessToken.contains("-")) {
      LOGGER.error("Access token null, empty or incorrect");
      throw new IllegalArgumentException();
    }
    return accessToken.substring(0, accessToken.indexOf("-"));
  }
}
