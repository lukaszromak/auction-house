import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import { AuthProvider } from './components/context/AuthContext';
import './App.css';
import Login from './components/Login';
import Home from './components/Home';
import ItemsList from './components/items/ItemsList';
import ItemForm from './components/items/ItemForm';
import NavbarMenu from './components/misc/NavbarMenu';
import Footer from './components/misc/Footer';
import Signup from './components/Signup';
import ItemPage from './components/items/ItemPage';
import AdminPage from './components/admin/AdminPage';
import ModeratorPage from './components/moderator/ModeratorPage';

function App() {
  return (
    <AuthProvider>
        <div className="d-flex flex-column min-vh-100">
        <Router>
          <NavbarMenu></NavbarMenu>
          <Routes>
            <Route path="/" element={<Home/>}></Route>
            <Route path="/login" element={<Login/>}></Route>
            <Route path="/items" element={<ItemsList/>}></Route>
            <Route path="/itemForm" element={<ItemForm/>}></Route>
            <Route path="/signup" element={<Signup/>}></Route>
            <Route path="/item/:itemId" element={<ItemPage/>}></Route>
            <Route path="/admin" element={<AdminPage/>}></Route>
            <Route path="/moderator" element={<ModeratorPage/>}></Route>
          </Routes>
          <Footer/>
        </Router>
        </div>
    </AuthProvider>
  );
}

export default App;
