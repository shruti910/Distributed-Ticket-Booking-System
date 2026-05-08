INSERT INTO VENUE (NAME, TOTAL_CAPACITY, ADDRESS)
VALUES ('Grand Garden Arena', 15000, 'Las Vegas, NV'),
       ('Madison Square Garden', 20000, 'New York, NY'),
       ('The O2 Arena', 20000, 'London, UK'),
       ('Red Rocks Amphitheatre', 9500, 'Morrison, CO'),
       ('Wembley Stadium', 90000, 'London, UK'),
       ('Saitama Super Arena', 36000, 'Saitama, Japan'),
       ('Hollywood Bowl', 17500, 'Los Angeles, CA'),
       ('Radio City Music Hall', 6000, 'New York, NY'),
       ('Sydney Opera House', 5700, 'Sydney, Australia'),
       ('The Fillmore', 1300, 'San Francisco, CA');

INSERT INTO EVENT (NAME, TOTAL_CAPACITY, REMAINING_CAPACITY, VENUE_ID, EVENT_DATE)
VALUES ('Summer Rock Fest', 15000, 15000, 1, '2026-07-15'),
       ('Pop World Tour', 20000, 20000, 2, '2026-08-20'),
       ('Jazz Night Out', 5700, 5700, 9, '2026-06-10'),
       ('Tech Conference 2026', 1300, 1300, 10, '2026-11-05'),
       ('Championship Finals', 90000, 90000, 5, '2026-05-30'),
       ('Classical Symphony', 6000, 6000, 8, '2026-09-12'),
       ('Indie Showcase', 9500, 9500, 4, '2026-10-01'),
       ('Electronic Beats', 36000, 36000, 6, '2026-12-20'),
       ('Comedy All-Stars', 17500, 17500, 7, '2026-07-22'),
       ('Winter Gala', 20000, 20000, 3, '2026-12-15');
