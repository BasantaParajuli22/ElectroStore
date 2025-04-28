import React from 'react'
import { Outlet } from 'react-router-dom';
import Navbar from './Navbar';
import Footer from './Footer';



function Layout() {
  return (
   <>
    <Navbar />
    <Outlet /> {/* This is where the child routes will be rendered */}
    <Footer />
   </>
  )
}

export default Layout