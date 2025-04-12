import './App.css'
import { Route, RouterProvider, createBrowserRouter, createRoutesFromElements } from 'react-router-dom'
import Layout from './pages/layout/Layout'
import Home from './pages/home/Home'
import Contact from './pages/home/Contact'
import About from './pages/home/About'
import ProductDetails from './components/products/ProductDetails'
import Products from './components/products/Products'
import ProductManager from './components/products/ProductManager'
import AuthSystem from './components/users/AuthSystem'



const router = createBrowserRouter(
  createRoutesFromElements(
    <Route path='/' element ={ <Layout /> } >
      <Route path='/' element= { <Home />} />
      <Route path='/shop' element= { <Products />} />
      <Route path='/products/:id' element= { <ProductDetails />} />
      <Route path='/products/manage' element= { <ProductManager />} />
      <Route path='/users' element= { <AuthSystem />} />

      <Route path='/contact' element= { <Contact />} />
      <Route path='/about' element= { <About />} />
    </Route>
  )
)


function App() {
  return (
  <RouterProvider router={ router }/>
  )
}

export default App
