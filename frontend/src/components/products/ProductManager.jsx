import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { fetchProducts } from '../../api/productApi';
import AddNewProductForm from './AddProductForm';
import UpdateProductForm from './UpdateProductForm';
import DeleteProduct from './DeleteProduct';
import AddProductNoFile from './AddProductNoFile';


const ProductManager = () => {
  const [products, setProducts] = useState([]);
  const [selectedProductId, setSelectedProductId] = useState(null);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const showProductDetails =(id) =>{
    navigate(`/products/${id}`);
  }

  useEffect(() => {
    const loadProducts = async () => {
      try {
        const data = await fetchProducts();
        setProducts(data);
      } catch (err) {
        setError(err.message);
      }
    };
    loadProducts();
  }, []);

  const handleProductAdded = (newProduct) => {
    // If API returns the complete product with ID, use this:
    setProducts(prev => [...prev, {
      ...newProduct,
      id: Number(newProduct.id)
  }]);
  };

  const handleProductUpdated = (updatedProduct) => {
    setProducts(prev =>
      prev.map(p => p.id === updatedProduct.id ? updatedProduct : p)
    );
    setSelectedProductId(null);
  };

  const handleProductDeleted = (deletedId) => {
    setProducts(prev => prev.filter(p => p.id !== deletedId));
  };

  const selectedProduct = products.find(p => p.id === selectedProductId);


  return (
    <div>
      <h1>Product Manager</h1>
      {error && <p style={{ color: 'red' }}>{error}</p>}

      <AddNewProductForm onProductAdded={handleProductAdded} />

      <h1> no image only details need to be filled up</h1>
      <AddProductNoFile onProductAdded={handleProductAdded} />

      {selectedProductId && (
        <UpdateProductForm
          productId={selectedProductId}
          onProductUpdate={handleProductUpdated}
        />
      )}

      <h2>Product List</h2>
      <ul>
        {products.map(product => (
          <li key={product.id}>
            {product.name} - ${product.price}
            <button onClick={() => setSelectedProductId( Number(product.id) ) }>
              Edit
            </button>
            <button className="add-to-cart-btn" onClick={ ()=> showProductDetails(product.id) }> View Details</button>
            <DeleteProduct
              productId={product.id}
              onDelete={handleProductDeleted}
            />
          </li>
        ))}
      </ul>
    </div>
  );
};

export default ProductManager;