package developer.remarks;

import developer.remarks.dao.PersonDAO;
import developer.remarks.dao.PrgDAO;
import developer.remarks.models.Book;
import developer.remarks.models.Content;
import developer.remarks.models.Music;
import developer.remarks.models.Person;
import developer.remarks.models.Prg;
import developer.remarks.services.MediaService;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Program {

    private static final Logger logger = Logger.getLogger(Program.class);

    public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/beans.xml");
		MediaService service = (MediaService) context.getBean("storageService");
		service.save(getBook());
		service.save(getTrack());
		logger.info("Список всех элементов библиотеки мультимедиа:");
		for (Content content : service.getAll()) {
			logger.info(content);
		}

		PersonDAO person = (PersonDAO) context.getBean("storagePerson");

		person.save(getPerson(1));
		person.save(getPerson(2));
		person.save(getPerson(3));
		
		logger.info("Список всех пользователей:");
		for (Person p : person.list()) {
			logger.info(p);
		}
		Person p = new Person();
		p.setName("Кайеров");
		person.update(2, p);

		logger.info("Измененный список всех пользователей:");
		for (Person p1 : person.list()) {
			logger.info(p1);
		}

		logger.info("------- PRG-----------");
		PrgDAO prg = (PrgDAO) context.getBean("storagePrg");

		
		logger.info("Список всех пользователей:");
		for (Prg p1 : prg.list()) {
			logger.info(p1);
		}
		

		
    }

    private static Person getPerson(int i) {
        Person person = new Person();
        if(i == 1)
           person.setName("Иванов");
        if(i == 2)
            person.setName("Сидоров");
        if(i == 3)
            person.setName("Петров");
        return person;
	}

	private static Content getBook() {
        Book book = new Book();
        book.setTitle("Над пропастью во ржи");
        book.getAuthor().setFirstName("Джером");
        book.getAuthor().setMiddleName("Дэвид");
        book.getAuthor().setLastName("Сэлинджер");
        book.setPageCount(500);
        return book;
    }

    private static Content getTrack() {
        Music track = new Music();
        track.setTitle("Moby - Lift Me Up");
        track.getAuthor().setFirstName("Ричард");
        track.getAuthor().setMiddleName("Мэлвилл");
        track.getAuthor().setLastName("Холл");
        track.setBitRate(256);
        return track;
    }

}
