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

import java.io.*; 
import java.lang.*;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Comparator;
import java.util.Collections;
import java.util.LinkedList;

public final class FindMeetingQuery {
  /**
   * Return time ranges when an event can be scheduled for all mandatory attendes of the meeting request.
   *
   * @param title The human-readable name for the event. Must be non-null.
   * @param when The time when the event takes place. Must be non-null.
   * @param attendees The collection of people attending the event. Must be non-null.
   */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

    // The meeting duration can not be longer than an entire day
    //TimeRange.WHOLE_DAY
    
    ArrayList<TimeRange> mandatoryAttendeeEventTimes = findEventTimes(events, request);
    LinkedList<TimeRange> unavailableTimes = determineUnavailableTimes(mandatoryAttendeeEventTimes);
    
    return determineAvailableTimes(unavailableTimes, request);

  }

  /**
   * Find all events that will be attended by any mandatory attendee of the request.
   *
   * @param title The human-readable name for the event. Must be non-null.
   * @param when The time when the event takes place. Must be non-null.
   * @param attendees The collection of people attending the event. Must be non-null.
   */
   private ArrayList<TimeRange> findEventTimes(Collection<Event> events, MeetingRequest request) {
       ArrayList<TimeRange> times = new ArrayList<TimeRange>();
        for (Event event : events) {
            HashSet<String> commonAttendees = new HashSet<String>();

            // Add the event attendees first because there tends to be fewer of them
            commonAttendees.addAll(event.getAttendees()); 
            // Ensure to only find availability of the requested attendees
            commonAttendees.retainAll(request.getAttendees()); 

            if (commonAttendees.size() > 0) { 
                times.add(event.getWhen());
            }
        }

        return times;
   }

  /**
   * Determine the overlap among all event times, using an O(nlogn) sorting algorithm and an O(n) linear pass to merge the TimeRanges.
   *
   * @param title The human-readable name for the event. Must be non-null.
   * @param when The time when the event takes place. Must be non-null.
   * @param attendees The collection of people attending the event. Must be non-null.
   */
   private LinkedList<TimeRange> determineUnavailableTimes(ArrayList<TimeRange> eventTimes) {

       // Sort the times to reduce time complexity by allowing us to identify overlap quicker
       eventTimes.sort(TimeRange.ORDER_BY_START);

       LinkedList<TimeRange> mergedeventTimes = new LinkedList<>();
       for (TimeRange tr : eventTimes) {
           // Append current TimeRange if there's no overlap with the last TimeRange or no TimeRanges have been merged yet
           if (mergedeventTimes.isEmpty() || !mergedeventTimes.getLast().overlaps(tr)) {
               mergedeventTimes.add(tr);
           }

           // Merge current and previous TimeRange if overlap exists by updating the last TimeRange -- can we always be sure last time?
           else {
               TimeRange lastTime = mergedeventTimes.getLast();

               // Identify the latest end of the merged Time Range
               int mergedEnd; 
               if (Long.compare(lastTime.end(), tr.end()) > 0) {
                   mergedEnd = lastTime.end();
               }
               else {
                   mergedEnd = tr.end();
               }

               mergedeventTimes.removeLast();
               mergedeventTimes.addLast(TimeRange.fromStartEnd(lastTime.start(),mergedEnd, false));
           }
        }

        return mergedeventTimes;
   }

  /**
   * Find the time in each day outside of the range of unavailable times.
   *
   * @param title The human-readable name for the event. Must be non-null.
   * @param when The time when the event takes place. Must be non-null.
   * @param attendees The collection of people attending the event. Must be non-null.
   */
    private ArrayList<TimeRange> determineAvailableTimes(LinkedList<TimeRange> unavailableTimes, MeetingRequest request) {

        ArrayList<TimeRange> availableTimes = new ArrayList<TimeRange>();
        long requestDuration = request.getDuration();

        // If no time conflicts are found, or there are no attendees present in the meeting request, and the meeting request is shorter than a whole day
        if (unavailableTimes.size() == 0) {
            if (requestDuration <= (long) TimeRange.WHOLE_DAY.duration())
                availableTimes.add(TimeRange.WHOLE_DAY);

        }
        else {
            // If the first event does not start at the beginning of the day, create availability until the first event if there is enough time
            if (unavailableTimes.getFirst().start() != TimeRange.START_OF_DAY) {
                if (unavailableTimes.getFirst().start() - TimeRange.START_OF_DAY >= requestDuration)
                    availableTimes.add(TimeRange.fromStartEnd(TimeRange.START_OF_DAY, unavailableTimes.getFirst().start(), false));
            }

            // Add availability between all unavailable times
            int i = 0; 
            while (i != unavailableTimes.size() - 1){ // does this catch for only 2 elements aka size 2 yes it runs through once
                TimeRange earlierTime = unavailableTimes.get(i);
                TimeRange laterTime = unavailableTimes.get(i + 1);

                // If the duration between two events is long enough, then the attendees are available in between the events, excluding the start time of the second event
                if (laterTime.start() - earlierTime.end() >= (requestDuration))
                    availableTimes.add(TimeRange.fromStartEnd(earlierTime.end(), laterTime.start(), false));

                i++;
            }    

            // If the last event does not end at the end of the day, create availability after the last event if there is enough time
            if (unavailableTimes.getLast().end() - 1 != TimeRange.END_OF_DAY) {
                if (TimeRange.END_OF_DAY - unavailableTimes.getLast().end() >= requestDuration)
                    availableTimes.add(TimeRange.fromStartEnd(unavailableTimes.getLast().end(), TimeRange.END_OF_DAY, true));
            }
        }
        return availableTimes;
    }
      
}
