package com.company.enroller.persistence;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("meetingService")
public class MeetingService {

    DatabaseConnector connector;

    public MeetingService() {
        connector = DatabaseConnector.getInstance();
    }

    public Collection<Meeting> getAll() {
        String hql = "FROM Meeting";
        Query query = connector.getSession().createQuery(hql);
        return query.list();
    }

    // THIS IS MY CODE FROM PREVIOUS TASK: https://moodle.lab.ii.agh.edu.pl/mod/assign/view.php?id=6737
    //
    public Meeting findById(String meetingId) {
        if (!meetingId.matches("[1-9]+[0-9]*")) {
            return null;
        }
        Session session = connector.getSession();
        return (Meeting) session.get(Meeting.class, Long.valueOf(meetingId));
    }

    public Collection<Meeting> findByTitle(String title) {
        Session session = connector.getSession();
        Query query = session.createQuery("FROM Meeting WHERE title='" + title + "'");
        return query.list();
    }

    public void add(Meeting meeting) {
        Session session = connector.getSession();
        Transaction transaction = session.beginTransaction();
        session.save(meeting);
        transaction.commit();
    }

    public void updateMeetingByAddingParticipant(Meeting meeting, Participant participant) {
        Session session = connector.getSession();
        Transaction transaction = session.beginTransaction();
        meeting.addParticipant(participant);
        session.update(meeting);
        transaction.commit();
    }

    public void delete(Collection<Meeting> meetings) {
        Session session = connector.getSession();
        Transaction transaction = session.beginTransaction();
        for (Meeting meeting : meetings) {
            session.delete(meeting);
        }
        transaction.commit();
    }

    public void update(Meeting meetingToUpdate, Meeting meetingNewData) {
        meetingToUpdate.setTitle(meetingNewData.getTitle());
        meetingToUpdate.setDate(meetingNewData.getDate());
        meetingToUpdate.setDescription(meetingNewData.getDescription());

        Session session = connector.getSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        session.update(meetingToUpdate);
        transaction.commit();
    }

    public void removeUser(Meeting meeting, Participant participant) {
        Session session = connector.getSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        meeting.removeParticipant(participant);
        session.update(meeting);
        transaction.commit();
    }

    public boolean isOnMeeting(Participant participant, Meeting meeting) {
        Collection<Participant> participants = meeting.getParticipants();
        return participants.contains(participant);
    }

}
