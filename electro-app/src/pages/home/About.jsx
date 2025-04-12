import React from 'react';
import './styles/About.css';

const About = () => {
  return (
    <div className="about-container">
      <section className="about-section">
        <h1 className="about-title">About ElectroStore</h1>
        <div className="about-content">
          <p>
            Founded in 2023, ElectroStore has quickly become a leading online retailer for electronics
            and tech gadgets. Our mission is to provide customers with the latest technology at
            competitive prices, backed by exceptional customer service.
          </p>
          <p>
            We carefully curate our product selection to include only the highest quality items from
            trusted brands. Whether you're looking for a new laptop, smartphone, or accessories,
            you'll find it at ElectroStore.
          </p>
          <p>
            Our team of tech enthusiasts is passionate about helping customers find the perfect
            products to meet their needs. We're constantly updating our inventory to include the
            newest releases and innovations in the tech world.
          </p>
        </div>
      </section>

      <section className="team-section">
        <h2 className="section-title">Our Team</h2>
        <div className="team-grid">
          <div className="team-card">
            <img
              src="https://cdn3.f-cdn.com/ppic/272619761/logo/82243977/jbNJr/CROPPED_profile_logo_JCYLP_8ee6ef69e11b638c99e9cd72bd8b5875.png?image-optimizer=force&format=webply&width=336"
              alt="Team Member"
              className="team-image"
            />
            <h3 className="team-member-name">Basanta Parajuli</h3>
            <p className="team-member-position">CEO & Founder</p>
          </div>
          <div className="team-card">
            <img
              src="https://via.placeholder.com/150"
              alt="Team Member"
              className="team-image"
            />
            <h3 className="team-member-name">Sarah Williams</h3>
            <p className="team-member-position">Head of Operations</p>
          </div>
          <div className="team-card">
            <img
              src="https://via.placeholder.com/150"
              alt="Team Member"
              className="team-image"
            />
            <h3 className="team-member-name">Michael Chen</h3>
            <p className="team-member-position">Tech Specialist</p>
          </div>
        </div>
      </section>
    </div>
  );
};

export default About;