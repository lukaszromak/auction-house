import { useState } from "react";
import { Pagination, Row } from "react-bootstrap";

function ItemPagination(props) {
    const {numPages, currentPage, setSearchParams, isBrowserChange} = props;

    const paginationBar = () => {
        let pages = [];
        if(numPages <= 9) {
            for(let i = 0; i < numPages; i++){
                pages.push(i + 1);
            }
        } else if(numPages > 9 && currentPage >= 0 && currentPage <= 6) {
            for(let i = 0; i < 7; i++){
                pages.push(i + 1);
            }
            pages.push(-1);
            pages.push(numPages)
        } else if(numPages > 9 && currentPage >= 7 && currentPage <= numPages - 8) {
            pages.push(1)
            pages.push(-1);
            for(let i = currentPage - 2; i <= currentPage + 2; i++){
                pages.push(i + 1);
            }
            pages.push(-1);
            pages.push(numPages)
        } else if(numPages > 9 && currentPage >= numPages - 7 && currentPage < numPages){
            pages.push(1);
            pages.push(-1);
            for(let i = numPages - 7; i < numPages; i++){
                pages.push(i + 1);
            }
        }

        const handlePageChange = (numPage) => {
            if(numPage >= 0 && numPage < numPages){
                isBrowserChange.current = false;
                setSearchParams(params => ({...params, page: numPage, browserChange: false}));
            }
        }

        return (
            <Pagination className="d-flex justify-content-center">
                <Pagination.Prev onClick={() => handlePageChange(currentPage - 1)}/>
                    {pages.map((page, idx) => (
                        page === -1 ? <Pagination.Ellipsis key={idx}/> : 
                        <Pagination.Item key={idx} active={page === currentPage + 1} onClick={() => handlePageChange(page - 1)}>{page}</Pagination.Item>
                    ))}
                <Pagination.Next onClick={() => handlePageChange(currentPage + 1)}/>
            </Pagination>
        );
    }

    return (
        paginationBar()
    );
}

export default ItemPagination;