// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Comparator;
import java.util.Collections;

public final class FindMeetingQuery {
  /**
   * Return time ranges when an event can be scheduled for all mandatory attendes of the meeting request.
   *
   * @param title The human-readable name for the event. Must be non-null.
   * @param when The time when the event takes place. Must be non-null.
   * @param attendees The collection of people attending the event. Must be non-null.
   */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    throw new UnsupportedOperationException("TODO: Implement this method.");
  }

  /**
   * Find all events that will be attended by any mandatory attendee of the request.
   *
   * @param title The human-readable name for the event. Must be non-null.
   * @param when The time when the event takes place. Must be non-null.
   * @param attendees The collection of people attending the event. Must be non-null.
   */
   private Collection<TimeRange> findEvents(Collection<Event> events, MeetingRequest request, QueryType type) {

   }

  /**
   * Determine the overlap between all event times. 
   *
   * @param title The human-readable name for the event. Must be non-null.
   * @param when The time when the event takes place. Must be non-null.
   * @param attendees The collection of people attending the event. Must be non-null.
   */
   private Collection<TimeRange> determineUnavailableTimes(Collection<Event> events, MeetingRequest request, QueryType type) {

   }

  /**
   * Find the time in each day outside of the range of unavailable times.
   *
   * @param title The human-readable name for the event. Must be non-null.
   * @param when The time when the event takes place. Must be non-null.
   * @param attendees The collection of people attending the event. Must be non-null.
   */
   private Collection<TimeRange> determineAvailableTimes(Collection<Event> events, MeetingRequest request, QueryType type) {

   }
      
}
