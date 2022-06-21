<template>
  <div>
    <new-meeting-form @added="addNewMeeting($event)"></new-meeting-form>

    <span v-if="meetings.length == 0">
               Brak zaplanowanych spotkań.
           </span>
    <h3 v-else>
      Wszystkie zaplanowane zajęcia ({{ meetings.length }})
    </h3>

    <meetings-list :meetings="meetings"
                   :username="username"
                   @attend="addMeetingParticipant($event)"
                   @unattend="removeMeetingParticipant($event)"
                   @delete="deleteMeeting($event)"></meetings-list>
  </div>
</template>

<script>
import NewMeetingForm from "./NewMeetingForm";
import MeetingsList from "./MeetingsList";

export default {
  components: {NewMeetingForm, MeetingsList},
  props: ['username'],
  mounted() {
    // IS RUNNING ON START-UP
    this.readAllMeetings();
  },
  data() {
    return {
      meetings: []
    };
  },
  methods: {
    readAllMeetings() {
      this.$http.get('meetings')
          .then(response => {
            this.meetings = response.body;
            console.log("Meetings were read again: " + this.meetings);
          })
          .catch(response => {
            console.log("Meetings were not read again - sth went wront.");
          });
    },

    addNewMeeting(meeting) {
      this.meetings.push(meeting);
      this.$http.post('meetings', meeting)
          .then(() => {
            console.log('Given meeting was added: ' + meeting.name + " " + meeting.description);
            this.readAllMeetings();
          })
          .catch(response => {
            console.log("Meeting was not created -> error");
          });
    },
    addMeetingParticipant(meeting) {
      meeting.participants.push(this.username);
    },
    removeMeetingParticipant(meeting) {
      meeting.participants.splice(meeting.participants.indexOf(this.username), 1);
    },
    deleteMeeting(meeting) {
      this.meetings.splice(this.meetings.indexOf(meeting), 1);
    }
  }
}
</script>
